package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.domain.*;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
import com.divertech.raxae.cobranca.repository.DespesaRepository;
import com.divertech.raxae.grupo.domain.Membro;
import com.divertech.raxae.grupo.domain.StatusParticipacao;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeracaoAutomaticaCobrancaService {

    private final DespesaRepository despesaRepository;
    private final CobrancaRepository cobrancaRepository;

    public int calcularDiaAlvo() {
        LocalDate dataAlvo = LocalDate.now().plusDays(3);
        return dataAlvo.getDayOfMonth();
    }

    public List<Despesa> buscarDespesasParaProcessar(int diaAlvo) {
        log.info("Buscando despesas com dia de vencimento: {}", diaAlvo);
        return despesaRepository.buscarPorDiaVencimentoEStatus(diaAlvo, StatusDespesa.ATIVA);
    }

    public boolean jaExistemCobrancasParaMes(Despesa despesa, String mesReferencia) {
        boolean existe = cobrancaRepository.existeCobrancaParaDespesaEMes(despesa.getId(), mesReferencia);
        if (existe) {
            log.info("Cobranças já existem para despesa {} no mês {}, ignorando...",
                    despesa.getNome(), mesReferencia);
        }
        return existe;
    }

    @Transactional
    public void gerarCobrancasParaDespesa(Despesa despesa, LocalDate dataVencimento) {
        log.info("Gerando cobranças para despesa: {} - Vencimento: {}",
                despesa.getNome(), dataVencimento);

        // Buscar membros ativos do grupo
        Set<Membro> membrosAtivos = despesa.getGrupo().getMembros().stream()
                .filter(m -> m.getStatus() == StatusParticipacao.ATIVO)
                .collect(Collectors.toSet());

        if (membrosAtivos.isEmpty()) {
            log.warn("Nenhum membro ativo encontrado para o grupo da despesa {}", despesa.getNome());
            throw APIException.build(HttpStatus.NOT_FOUND, "Nenhum membro ativo encontrado para o grupo da despesa.");
        }

        List<Cobranca> cobrancas = new ArrayList<>();

        // Calcular valores individuais conforme tipo de divisão
        if (despesa.getTipoDivisao() == TipoDivisao.IGUALITARIA) {
            // Divisão igualitária: valor dividido igualmente entre todos os membros ativos
            BigDecimal valorPorMembro = despesa.getValor()
                    .divide(BigDecimal.valueOf(membrosAtivos.size()), 2, RoundingMode.HALF_UP);

            for (Membro membro : membrosAtivos) {
                Cobranca cobranca = new Cobranca(
                        despesa,
                        membro.getUsuario(),
                        valorPorMembro,
                        StatusCobranca.PENDENTE,
                        dataVencimento
                );
                cobrancas.add(cobranca);
            }
        } else if (despesa.getTipoDivisao() == TipoDivisao.POR_VALOR) {
            // Divisão personalizada por valor
            Map<UUID, BigDecimal> divisoesEspecificas = despesa.getDivisoesEspecificas();

            if (divisoesEspecificas == null || divisoesEspecificas.isEmpty()) {
                log.warn("Despesa {} é do tipo POR_VALOR mas não possui divisões específicas definidas", despesa.getNome());
                throw APIException.build(HttpStatus.BAD_REQUEST, "Divisões específicas não definidas para despesa do tipo POR_VALOR");
            }

            for (Membro membro : membrosAtivos) {
                UUID usuarioId = membro.getId();
                BigDecimal valorMembro = divisoesEspecificas.getOrDefault(usuarioId, BigDecimal.ZERO);

                if (valorMembro.compareTo(BigDecimal.ZERO) > 0) {
                    Cobranca cobranca = new Cobranca(
                            despesa,
                            membro.getUsuario(),
                            valorMembro,
                            StatusCobranca.PENDENTE,
                            dataVencimento
                    );
                    cobrancas.add(cobranca);
                }
            }
        }

        // Bulk Insert: salvar todas as cobranças de uma vez
        if (!cobrancas.isEmpty()) {
            cobrancaRepository.salvarVarias(cobrancas);
            log.info("Geradas {} cobranças para a despesa {}", cobrancas.size(), despesa.getNome());
        } else {
            log.warn("Nenhuma cobrança foi gerada para a despesa {}", despesa.getNome());
        }
    }

    @Transactional
    public void executarGeracaoAutomatica() {
        log.info("=== Iniciando processo de geração automática de cobranças ===");

        // Passo 1: Calcular dia-alvo (data atual + 3 dias)
        int diaAlvo = calcularDiaAlvo();

        // Calcular ano e mês atual para a data de vencimento
        YearMonth mesAtual = YearMonth.now();
        String mesReferencia = mesAtual.toString();

        log.info("Dia alvo: {} - Mês de referência: {}", diaAlvo, mesReferencia);

        // Passo 2: Buscar despesas relevantes
        List<Despesa> despesas = buscarDespesasParaProcessar(diaAlvo);
        log.info("Encontradas {} despesas para processar", despesas.size());

        int despesasProcessadas = 0;
        int despesasIgnoradas = 0;

        for (Despesa despesa : despesas) {
            try {
                // Passo 3: Verificar idempotência
                if (jaExistemCobrancasParaMes(despesa, mesReferencia)) {
                    despesasIgnoradas++;
                    continue;
                }
                if(despesa.getTipoRecorrencia() != TipoRecorrencia.UNICA){
                    // Calcular data de vencimento: dia da despesa no mês/ano atual
                    int diaVencimento = despesa.getDiaVencimento();
                    LocalDate dataVencimento = mesAtual.atDay(Math.min(diaVencimento, mesAtual.lengthOfMonth()));

                    // Passo 4: Gerar cobranças
                    gerarCobrancasParaDespesa(despesa, dataVencimento);
                    despesasProcessadas++;
                }

            } catch (Exception e) {
                log.error("Erro ao processar despesa {}: {}", despesa.getNome(), e.getMessage(), e);
            }
        }
        log.info("=== Processo finalizado ===");
        log.info("Despesas processadas: {}", despesasProcessadas);
        log.info("Despesas ignoradas (já tinham cobranças): {}", despesasIgnoradas);
        log.info("=====================================");
    }
}
