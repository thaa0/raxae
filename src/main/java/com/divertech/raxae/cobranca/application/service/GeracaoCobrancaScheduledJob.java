package com.divertech.raxae.cobranca.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Job agendado para geração automática de cobranças de despesas recorrentes
 * Executa diariamente às 05:00 da manhã
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GeracaoCobrancaScheduledJob {

    private final GeracaoAutomaticaCobrancaService geracaoAutomaticaCobrancaService;

    /**
     * CRON Job que executa todos os dias às 05:00
     * Formato CRON: segundo minuto hora dia mês dia-da-semana
     * "0 0 5 * * ?" = 0 segundos, 0 minutos, 5 horas, todo dia, todo mês, qualquer dia da semana
     */
    @Scheduled(cron = "0 0 5 * * ?")
    public void executarGeracaoAutomaticaCobrancas() {
        log.info("========================================");
        log.info("CRON Job iniciado: Geração Automática de Cobranças");
        log.info("========================================");

        try {
            geracaoAutomaticaCobrancaService.executarGeracaoAutomatica();
            log.info("CRON Job finalizado com sucesso");
        } catch (Exception e) {
            log.error("Erro durante execução do CRON Job de geração de cobranças", e);
        }

        log.info("========================================");
    }
}

