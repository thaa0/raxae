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


@Service
@RequiredArgsConstructor
@Slf4j
public class GeracaoLembreteService {

    private final CobrancaRepository cobrancaRepository;
    private final WhatsAppService whatsAppService;
    private final MensagemLembreteBuilder mensagemBuilder;

    private List<Cobranca> buscarCobrancasPendentes(LocalDate dataVencimento) {
        String mesReferencia = YearMonth.from(LocalDate.now()).toString();
        return cobrancaRepository.buscarPorStatusMesReferenciaEDataVencimento(
                StatusCobranca.PENDENTE,
                mesReferencia,
                dataVencimento
        );
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceDaqui2Dias() {
        LocalDate daquiDoisDias = LocalDate.now().plusDays(2);
        log.info("Processando lembretes: Vence Daqui 2 Dias ({})", daquiDoisDias);

        List<Cobranca> cobrancas = buscarCobrancasPendentes(daquiDoisDias);
        log.info("Encontradas {} cobranças que vencem daqui 2 dias", cobrancas.size());

        int sucessos = 0;
        for (Cobranca cobranca : cobrancas) {
            try {
                String numeroWhatsApp = cobranca.getUsuario().getWhatsapp();
                String nomeDespesa = cobranca.getDespesa().getNome();
                String nomeUsuario = cobranca.getUsuario().getNomeCompleto().split(" ")[0];

                String mensagem = mensagemBuilder.construirMensagem(
                        TipoLembrete.VENCE_DAQUI_2_DIAS,
                        nomeDespesa,
                        nomeUsuario
                );

                boolean enviado = whatsAppService.enviarMensagem(numeroWhatsApp, mensagem);
                if (enviado) {
                    sucessos++;
                }

            } catch (Exception e) {
                log.error("Erro ao processar lembrete vence daqui 2 dias para cobrança {}: {}",
                        cobranca.getId(), e.getMessage(), e);
            }
        }

        log.info("Lembretes 'Vence Daqui 2 Dias' enviados: {}/{}", sucessos, cobrancas.size());
        return sucessos;
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceAmanha() {
        LocalDate amanha = LocalDate.now().plusDays(1);
        log.info("Processando lembretes: Vence Amanhã ({})", amanha);

        List<Cobranca> cobrancas = buscarCobrancasPendentes(amanha);
        log.info("Encontradas {} cobranças que vencem amanhã", cobrancas.size());

        int sucessos = 0;
        for (Cobranca cobranca : cobrancas) {
            try {
                String numeroWhatsApp = cobranca.getUsuario().getWhatsapp();
                String nomeDespesa = cobranca.getDespesa().getNome();
                String nomeUsuario = cobranca.getUsuario().getNomeCompleto().split(" ")[0];

                String mensagem = mensagemBuilder.construirMensagem(
                        TipoLembrete.VENCE_AMANHA,
                        nomeDespesa,
                        nomeUsuario
                );

                boolean enviado = whatsAppService.enviarMensagem(numeroWhatsApp, mensagem);
                if (enviado) {
                    sucessos++;
                }

            } catch (Exception e) {
                log.error("Erro ao processar lembrete vence amanhã para cobrança {}: {}",
                        cobranca.getId(), e.getMessage(), e);
            }
        }

        log.info("Lembretes 'Vence Amanhã' enviados: {}/{}", sucessos, cobrancas.size());
        return sucessos;
    }

    @Transactional(readOnly = true)
    public int processarLembretesVenceHoje() {
        LocalDate hoje = LocalDate.now();
        log.info("Processando lembretes: Vence Hoje ({})", hoje);

        List<Cobranca> cobrancas = buscarCobrancasPendentes(hoje);
        log.info("Encontradas {} cobranças que vencem hoje", cobrancas.size());

        int sucessos = 0;
        for (Cobranca cobranca : cobrancas) {
            try {
                String numeroWhatsApp = cobranca.getUsuario().getWhatsapp();
                String nomeDespesa = cobranca.getDespesa().getNome();
                String nomeUsuario = cobranca.getUsuario().getNomeCompleto().split(" ")[0];

                String mensagem = mensagemBuilder.construirMensagem(
                        TipoLembrete.VENCE_HOJE,
                        nomeDespesa,
                        nomeUsuario
                );

                boolean enviado = whatsAppService.enviarMensagem(numeroWhatsApp, mensagem);
                if (enviado) {
                    sucessos++;
                }

            } catch (Exception e) {
                log.error("Erro ao processar lembrete vence hoje para cobrança {}: {}",
                        cobranca.getId(), e.getMessage(), e);
            }
        }

        log.info("Lembretes 'Vence Hoje' enviados: {}/{}", sucessos, cobrancas.size());
        return sucessos;
    }


    public void executarGeracaoLembretes() {
        log.info("========================================");
        log.info("=== Iniciando Geração de Lembretes Automáticos ===");
        log.info("========================================");

        int totalEnviados = 0;

        try {
            // Janela 1: Vence daqui 2 dias
            int venceDaqui2Dias = processarLembretesVenceDaqui2Dias();
            totalEnviados += venceDaqui2Dias;

            // Janela 2: Vence amanhã (daqui 1 dia)
            int venceAmanha = processarLembretesVenceAmanha();
            totalEnviados += venceAmanha;

            // Janela 3: Vence hoje
            int venceHoje = processarLembretesVenceHoje();
            totalEnviados += venceHoje;

            log.info("========================================");
            log.info("=== Resumo da Execução ===");
            log.info("Lembretes 'Vence Daqui 2 Dias': {}", venceDaqui2Dias);
            log.info("Lembretes 'Vence Amanhã': {}", venceAmanha);
            log.info("Lembretes 'Vence Hoje': {}", venceHoje);
            log.info("Total de lembretes enviados: {}", totalEnviados);
            log.info("========================================");

        } catch (Exception e) {
            log.error("Erro durante execução da geração de lembretes", e);
        }
    }

}