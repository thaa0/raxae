package com.divertech.raxae.notificacao.application.service;

import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.domain.StatusCobranca;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
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

    private static final int DIAS_ANTECEDENCIA_LEMBRETE_1 = 2;
    private static final int DIAS_ANTECEDENCIA_LEMBRETE_2 = 1;
    private static final int DIAS_ANTECEDENCIA_LEMBRETE_3 = 0;

    public void executarGeracaoLembretes() {
        executarGeracaoLembretesInterno(Optional.empty());
    }

    public void executarGeracaoLembretes(UUID idGrupo) {
        executarGeracaoLembretesInterno(Optional.of(idGrupo));
    }

    private void executarGeracaoLembretesInterno(Optional<UUID> idGrupo) {
        String contextoGrupo = idGrupo.map(id -> " para o Grupo " + id).orElse("");
        log.info("=== Iniciando Geração de Lembretes Automáticos{} ===", contextoGrupo);

        int totalEnviados = 0;
        try {
            int venceDaqui2Dias = idGrupo.map(this::processarLembretesVenceDaqui2Dias)
                    .orElseGet(this::processarLembretesVenceDaqui2Dias);
            totalEnviados += venceDaqui2Dias;

            int venceAmanha = idGrupo.map(this::processarLembretesVenceAmanha)
                    .orElseGet(this::processarLembretesVenceAmanha);
            totalEnviados += venceAmanha;

            int venceHoje = idGrupo.map(this::processarLembretesVenceHoje)
                    .orElseGet(this::processarLembretesVenceHoje);
            totalEnviados += venceHoje;

            logResumoExecucao(venceDaqui2Dias, venceAmanha, venceHoje, totalEnviados, idGrupo);
        } catch (Exception e) {
            log.error("[error] Erro durante execução da geração de lembretes{}", contextoGrupo, e);
        }
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceDaqui2Dias() {
        LocalDate daquiDoisDias = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_1);
        return processarLembretes(TipoLembrete.VENCE_DAQUI_2_DIAS, daquiDoisDias, Optional.empty());
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceDaqui2Dias(UUID idGrupo) {
        LocalDate daquiDoisDias = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_1);
        return processarLembretes(TipoLembrete.VENCE_DAQUI_2_DIAS, daquiDoisDias, Optional.of(idGrupo));
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceAmanha() {
        LocalDate amanha = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_2);
        return processarLembretes(TipoLembrete.VENCE_AMANHA, amanha, Optional.empty());
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceAmanha(UUID idGrupo) {
        LocalDate amanha = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_2);
        return processarLembretes(TipoLembrete.VENCE_AMANHA, amanha, Optional.of(idGrupo));
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceHoje() {
        LocalDate hoje = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_3);
        return processarLembretes(TipoLembrete.VENCE_HOJE, hoje, Optional.empty());
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceHoje(UUID idGrupo) {
        LocalDate hoje = LocalDate.now().plusDays(DIAS_ANTECEDENCIA_LEMBRETE_3);
        return processarLembretes(TipoLembrete.VENCE_HOJE, hoje, Optional.of(idGrupo));
    }

    private int processarLembretes(TipoLembrete tipoLembrete, LocalDate dataVencimento, Optional<UUID> idGrupo) {
        String contextoGrupo = idGrupo.map(id -> " para o Grupo " + id).orElse("");
        log.info("Processando lembretes: {} ({}){}", tipoLembrete, dataVencimento, contextoGrupo);

        List<Cobranca> cobrancas = buscarCobrancasPendentes(dataVencimento, idGrupo);
        log.info("Encontradas {} cobranças que {} {}",
                cobrancas.size(),
                obterDescricaoVencimento(tipoLembrete),
                contextoGrupo);

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
                tipoLembrete, contextoGrupo, sucessos, cobrancas.size());
        return sucessos;
    }

    private void logResumoExecucao(int venceDaqui2Dias, int venceAmanha, int venceHoje,
                                   int totalEnviados, Optional<UUID> idGrupo) {
        String contextoGrupo = idGrupo.map(id -> " para o Grupo " + id).orElse("");

        log.info("========================================");
        log.info("=== Resumo da Execução{} ===", contextoGrupo);
        log.info("Lembretes 'Vence Daqui 2 Dias': {}", venceDaqui2Dias);
        log.info("Lembretes 'Vence Amanhã': {}", venceAmanha);
        log.info("Lembretes 'Vence Hoje': {}", venceHoje);
        log.info("Total de lembretes enviados: {}", totalEnviados);
    }

    private List<Cobranca> buscarCobrancasPendentes(LocalDate dataVencimento, Optional<UUID> idGrupo) {
        String mesReferencia = YearMonth.from(LocalDate.now()).toString();

        if (idGrupo.isPresent()) {
            return cobrancaRepository.buscarPorStatusMesReferenciaDataVencimentoEGrupo(
                    StatusCobranca.PENDENTE,
                    mesReferencia,
                    dataVencimento,
                    idGrupo.get()
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

        String mensagem = mensagemBuilder.construirMensagem(
                tipoLembrete,
                nomeDespesa,
                nomeUsuario
        );

        log.debug("{} - {}", nomeDespesa, mensagem);
        return whatsAppService.enviarMensagem(numeroWhatsApp, mensagem);
    }

    private String extrairPrimeiroNome(String nomeCompleto) {
        return nomeCompleto.split(" ")[0];
    }
}