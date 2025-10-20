package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.application.controller.DespesaRequest;
import com.divertech.raxae.cobranca.domain.*;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
import com.divertech.raxae.cobranca.repository.DespesaRepository;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.grupo.domain.Membro;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.divertech.raxae.cobranca.domain.TipoRecorrencia;
import com.divertech.raxae.cobranca.domain.TipoDivisao;
import com.divertech.raxae.cobranca.domain.StatusCobranca;
import com.divertech.raxae.cobranca.application.port.out.NotificacaoServicePort;

import com.divertech.raxae.cobranca.domain.StatusDespesa; 

@Service
@RequiredArgsConstructor
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final CobrancaRepository cobrancaRepository;
    private final NotificacaoServicePort notificacaoPort;

    @Transactional
    public Despesa registrarDespesa(Grupo grupo, Usuario admin, DespesaRequest request) {
        validarVencimento(request);
        Despesa despesa = new Despesa(grupo, admin, request);
        Despesa despesaSalva = despesaRepository.salvar(despesa);
        Set<Membro> membrosDoGrupo = grupo.getMembros(); 
        List<Usuario> usuariosDoGrupo = membrosDoGrupo.stream()
                .map(Membro::getUsuario) 
                .collect(Collectors.toList());
        List<Cobranca> cobrancas = calcularDivisao(despesaSalva, usuariosDoGrupo, request);
        cobrancaRepository.salvarVarias(cobrancas);

        String mensagem = String.format("Nova despesa '%s' (R$%.2f) no grupo '%s'.",
                despesa.getNome(), despesa.getValor(), grupo.getNomeGrupo());
        notificacaoPort.notificarMembros(usuariosDoGrupo, mensagem);

        return despesaSalva;
    }

    @Transactional
    public void cancelarDespesa(Despesa despesa) {
        List<Cobranca> cobrancas = cobrancaRepository.buscaPorIdDaDespesa(despesa.getId());

        List<Cobranca> cobrancasParaSalvar = new ArrayList<>();
        for (Cobranca cobranca : cobrancas) {
            if (cobranca.getStatus() == StatusCobranca.PENDENTE) {
                cobranca.setStatus(StatusCobranca.CANCELADA);
                cobrancasParaSalvar.add(cobranca);
            }
        }

        if (!cobrancasParaSalvar.isEmpty()) {
            cobrancaRepository.salvarVarias(cobrancasParaSalvar);
        }

        despesa.setStatus(StatusDespesa.CANCELADA);
        despesaRepository.salvar(despesa);
    }
    private void validarVencimento(DespesaRequest request) {
        if (request.getTipoRecorrencia() == TipoRecorrencia.UNICA) {
            if (request.getDataVencimentoAvulsa() == null) {
                throw APIException.build(HttpStatus.BAD_REQUEST, "Para despesas AVULSAS, 'dataVencimentoAvulsa' é obrigatório.");
            }
        } else if (request.getTipoRecorrencia() == TipoRecorrencia.MENSAL) {
            if (request.getDiaVencimento() == null) {
                throw APIException.build(HttpStatus.BAD_REQUEST, "Para despesas RECORRENTES, 'diaVencimento' é obrigatório.");
            }
        }
    }

    private List<Cobranca> calcularDivisao(Despesa despesa, List<Usuario> usuarios, DespesaRequest request) {
        List<Cobranca> cobrancas = new ArrayList<>();
        Map<UUID, Usuario> mapaMembros = usuarios.stream()
                .collect(Collectors.toMap(Usuario::getId, usuario -> usuario));
        LocalDate dataVencimento = calcularDataVencimento(despesa);
        switch (despesa.getTipoDivisao()) {
            case IGUALITARIA:
                BigDecimal valorIndividual = despesa.getValor()
                        .divide(new BigDecimal(usuarios.size()), 2, RoundingMode.HALF_UP);
                for (Usuario usuario : usuarios) {
                    cobrancas.add(criarCobranca(despesa, usuario, valorIndividual, dataVencimento));
                }
                break;
            case POR_VALOR:
                validarDivisaoExata(despesa.getValor(), request.getDivisoesEspecificas());
                for (Map.Entry<UUID, BigDecimal> entry : request.getDivisoesEspecificas().entrySet()) {
                    Usuario devedor = mapaMembros.get(entry.getKey());
                    if (devedor == null) {
                        throw APIException.build(HttpStatus.BAD_REQUEST, "Usuário com ID " + entry.getKey() + " não pertence a este grupo.");
                    }
                    cobrancas.add(criarCobranca(despesa, devedor, entry.getValue(), dataVencimento));
                }
                break;
            case POR_PERCENTUAL:
                validarDivisaoPorcentagem(request.getDivisoesEspecificas());
                for (Map.Entry<UUID, BigDecimal> entry : request.getDivisoesEspecificas().entrySet()) {
                    Usuario devedor = mapaMembros.get(entry.getKey());
                    if (devedor == null) {
                        throw APIException.build(HttpStatus.BAD_REQUEST, "Usuário com ID " + entry.getKey() + " não pertence a este grupo.");
                    }
                    BigDecimal porcentagem = entry.getValue().divide(new BigDecimal(100));
                    BigDecimal valorCalculado = despesa.getValor().multiply(porcentagem).setScale(2, RoundingMode.HALF_UP);
                    cobrancas.add(criarCobranca(despesa, devedor, valorCalculado, dataVencimento));
                }
                break;
        }
        return cobrancas;
    }

    private LocalDate calcularDataVencimento(Despesa despesa) {
        if (despesa.getTipoRecorrencia() == TipoRecorrencia.UNICA) {
            return despesa.getDataVencimentoAvulsa();
        } else {
            LocalDate hoje = LocalDate.now();
            LocalDate vencimentoEsteMes = hoje.withDayOfMonth(despesa.getDiaVencimento());
            if (vencimentoEsteMes.isBefore(hoje)) {
                return vencimentoEsteMes.plusMonths(1);
            }
            return vencimentoEsteMes;
        }
    }

    private Cobranca criarCobranca(Despesa despesa, Usuario devedor, BigDecimal valor, LocalDate dataVencimento) {
        return new Cobranca(despesa, devedor, valor, StatusCobranca.PENDENTE, dataVencimento);
    }

    private void validarDivisaoExata(BigDecimal valorTotal, Map<UUID, BigDecimal> divisoes) {
        if (divisoes == null || divisoes.isEmpty()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Para divisão por VALOR_EXATO, o mapa 'divisoesEspecificas' é obrigatório.");
        }
        BigDecimal soma = divisoes.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (soma.compareTo(valorTotal) != 0) {
            throw APIException.build(HttpStatus.BAD_REQUEST,
                    String.format("A soma dos valores (R$%.2f) não bate com o valor total da despesa (R$%.2f).", soma, valorTotal)
            );
        }
    }
    
    private void validarDivisaoPorcentagem(Map<UUID, BigDecimal> divisoes) {
        if (divisoes == null || divisoes.isEmpty()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Para divisão por PORCENTAGEM, o mapa 'divisoesEspecificas' é obrigatório.");
        }
        BigDecimal somaPercentual = divisoes.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (somaPercentual.compareTo(new BigDecimal(100)) != 0) {
            throw APIException.build(HttpStatus.BAD_REQUEST,
                    String.format("A soma das porcentagens (%.2f%%) não totaliza 100%%.", somaPercentual)
            );
        }
    }
}