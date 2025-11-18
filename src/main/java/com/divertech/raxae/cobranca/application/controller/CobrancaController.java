package com.divertech.raxae.cobranca.application.controller;

import com.divertech.raxae.cobranca.application.service.GeracaoAutomaticaCobrancaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para execução manual do processo de geração de cobranças
 * Útil para testes e execuções pontuais
 */
@RestController
@RequestMapping("/cobrancas")
@RequiredArgsConstructor
@Tag(name = "Cobranças", description = "Endpoints relacionados a cobranças")
public class CobrancaController {

    private final GeracaoAutomaticaCobrancaService geracaoAutomaticaCobrancaService;

    @PostMapping("/gerar-automaticas")
    @Operation(summary = "Gerar cobranças automáticas manualmente",
               description = "Executa o processo de geração automática de cobranças para despesas recorrentes. " +
                           "Normalmente este processo roda automaticamente às 05:00 via CRON Job.")
    public ResponseEntity<String> gerarCobrancasAutomaticas() {
        geracaoAutomaticaCobrancaService.executarGeracaoAutomatica();
        return ResponseEntity.ok("Processo de geração automática de cobranças executado com sucesso. Verifique os logs para detalhes.");
    }
}

