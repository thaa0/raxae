package com.divertech.raxae.notificacao.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Job agendado para envio automático de lembretes via WhatsApp
 * Executa diariamente às 09:00 da manhã
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LembreteCobrancaScheduledJob {

    private final GeracaoLembreteService geracaoLembreteService;

    /**
     * CRON Job que executa todos os dias às 09:00
     * Formato CRON: segundo minuto hora dia mês dia-da-semana
     * "0 0 9 * * ?" = 0 segundos, 0 minutos, 9 horas, todo dia, todo mês, qualquer dia da semana
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void executarEnvioLembretesAutomaticos() {
        log.info("========================================");
        log.info("CRON Job iniciado: Envio de Lembretes via WhatsApp");
        log.info("========================================");

        try {
            geracaoLembreteService.executarGeracaoLembretes();
            log.info("CRON Job de lembretes finalizado com sucesso");
        } catch (Exception e) {
            log.error("Erro durante execução do CRON Job de lembretes", e);
        }

        log.info("========================================");
    }
}
