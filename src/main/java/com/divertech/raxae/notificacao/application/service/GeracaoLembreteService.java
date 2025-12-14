package com.divertech.raxae.notificacao.application.service;

import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.domain.StatusCobranca;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
import com.divertech.raxae.cobranca.repository.DespesaRepository;
import com.divertech.raxae.notificacao.domain.TipoLembrete;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class GeracaoLembreteService {
    private final CobrancaRepository cobrancaRepository;
    private final WhatsAppService whatsAppService;
    private final MensagemLembreteBuilder mensagemBuilder;
    private final DespesaRepository despesaRepository;

    private static final int DIAS_ANTECEDENCIA_LEMBRETE_1 = 2;
    private static final int DIAS_ANTECEDENCIA_LEMBRETE_2 = 1;
    private static final int DIAS_ANTECEDENCIA_LEMBRETE_3 = 0;

    public void executarGeracaoLembretes() {
        executarGeracaoLembretesInterno(Optional.empty());
    }

    public void executarGeracaoLembretes(UUID idDespesa) {
        executarGeracaoLembretesInterno(Optional.of(idDespesa));
    }

    private void executarGeracaoLembretesInterno(Optional<UUID> idDespesa) {
        String contextoDespesa = idDespesa.map(id -> " para o Despesa " + id).orElse("");
        log.info("=== Iniciando Geração de Lembretes Automáticos{} ===", contextoDespesa);

        int totalEnviados = 0;
        try {
            int venceDaqui2Dias = idDespesa.map(this::processarLembretesVenceDaqui2Dias)
                    .orElseGet(this::processarLembretesVenceDaqui2Dias);
            totalEnviados += venceDaqui2Dias;

            int venceAmanha = idDespesa.map(this::processarLembretesVenceAmanha)
                    .orElseGet(this::processarLembretesVenceAmanha);
            totalEnviados += venceAmanha;

            int venceHoje = idDespesa.map(this::processarLembretesVenceHoje)
                    .orElseGet(this::processarLembretesVenceHoje);
            totalEnviados += venceHoje;

            logResumoExecucao(venceDaqui2Dias, venceAmanha, venceHoje, totalEnviados, idDespesa);
        } catch (Exception e) {
            log.error("[error] Erro durante execução da geração de lembretes{}", contextoDespesa, e);
        }
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceDaqui2Dias() {
        LocalDate daquiDoisDias = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_1);
        return processarLembretes(TipoLembrete.VENCE_DAQUI_2_DIAS, daquiDoisDias, Optional.empty());
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceDaqui2Dias(UUID idDespesa) {
        LocalDate daquiDoisDias = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_1);
        return processarLembretes(TipoLembrete.VENCE_DAQUI_2_DIAS, daquiDoisDias, Optional.of(idDespesa));
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceAmanha() {
        LocalDate amanha = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_2);
        return processarLembretes(TipoLembrete.VENCE_AMANHA, amanha, Optional.empty());
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceAmanha(UUID idDespesa) {
        LocalDate amanha = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_2);
        return processarLembretes(TipoLembrete.VENCE_AMANHA, amanha, Optional.of(idDespesa));
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceHoje() {
        LocalDate hoje = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_3);
        return processarLembretes(TipoLembrete.VENCE_HOJE, hoje, Optional.empty());
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceHoje(UUID idDespesa) {
        LocalDate hoje = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_3);
        return processarLembretes(TipoLembrete.VENCE_HOJE, hoje, Optional.of(idDespesa));
    }

    private int processarLembretes(TipoLembrete tipoLembrete, LocalDate dataVencimento, Optional<UUID> idDespesa) {
        String contextoDespesa = idDespesa.map(id -> " para a Despesa " + id).orElse("");
        log.info("Processando lembretes: {} ({}){}", tipoLembrete, dataVencimento, contextoDespesa);

        List<Cobranca> cobrancas = buscarCobrancasPendentes(dataVencimento, idDespesa);
        log.info("Encontradas {} cobranças que {} {}",
                cobrancas.size(),
                obterDescricaoVencimento(tipoLembrete),
                contextoDespesa);

        int sucessos = 0;
        for (Cobranca cobranca : cobrancas) {
            try {
                if (enviarLembreteParaCobranca(cobranca, tipoLembrete)) {
                    sucessos++;
                }
            } catch (Exception e) {
                log.error("Erro ao processar lembrete {} para cobrança {}: {}",
                        tipoLembrete, cobranca.getId(), e.getMessage(), e);
            }
        }

        log.info("Lembretes '{}' enviados{}: {}/{}",
                tipoLembrete, contextoDespesa, sucessos, cobrancas.size());
        return sucessos;
    }

    private void logResumoExecucao(int venceDaqui2Dias, int venceAmanha, int venceHoje,
                                   int totalEnviados, Optional<UUID> idDespesa) {
        String contextoDespesa = idDespesa.map(id -> " para o Despesa " + id).orElse("");

        log.info("========================================");
        log.info("=== Resumo da Execução{} ===", contextoDespesa);
        log.info("Lembretes 'Vence Daqui 2 Dias': {}", venceDaqui2Dias);
        log.info("Lembretes 'Vence Amanhã': {}", venceAmanha);
        log.info("Lembretes 'Vence Hoje': {}", venceHoje);
        log.info("Total de lembretes enviados: {}", totalEnviados);
    }

    private List<Cobranca> buscarCobrancasPendentes(LocalDate dataVencimento, Optional<UUID> idDespesa) {
        String mesReferencia = YearMonth.from(LocalDate.now()).toString();

        if (idDespesa.isPresent()) {
            return cobrancaRepository.buscarPorStatusMesReferenciaDataVencimentoEDespesa(
                    StatusCobranca.PENDENTE,
                    mesReferencia,
                    dataVencimento,
                    idDespesa.get()
            );
        }

        return cobrancaRepository.buscarPorStatusMesReferenciaEDataVencimento(
                StatusCobranca.PENDENTE,
                mesReferencia,
                dataVencimento
        );
    }

    private String obterDescricaoVencimento(TipoLembrete tipoLembrete) {
        switch (tipoLembrete) {
            case VENCE_DAQUI_2_DIAS:
                return "vencem daqui 2 dias";
            case VENCE_AMANHA:
                return "vencem amanhã";
            case VENCE_HOJE:
                return "vencem hoje";
            default:
                return "vencem";
        }
    }

    private boolean enviarLembreteParaCobranca(Cobranca cobranca, TipoLembrete tipoLembrete) {
        String numeroWhatsApp = cobranca.getUsuario().getWhatsapp();
        String nomeDespesa = cobranca.getDespesa().getNome();
        String nomeUsuario = extrairPrimeiroNome(cobranca.getUsuario().getNomeCompleto());
        String pixBeneficiario = despesaRepository.buscaPorId(cobranca.getDespesa().getId()).getPixBeneficiado();
        String mensagem = mensagemBuilder.construirMensagem(
                tipoLembrete,
                nomeDespesa,
                nomeUsuario,
                pixBeneficiario
        );

        log.debug("{} - {}", nomeDespesa, mensagem);
        return whatsAppService.enviarMensagem("557388805168", mensagem);
    }

    private String extrairPrimeiroNome(String nomeCompleto) {
        return nomeCompleto.split(" ")[0];
    }
}