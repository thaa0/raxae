package com.divertech.raxae.notificacao.application.controller;

import com.divertech.raxae.notificacao.application.service.GeracaoLembreteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller para execução manual de lembretes de cobrança
 */
@RestController
@RequestMapping("/lembretes")
@RequiredArgsConstructor
@Tag(name = "Lembretes", description = "Endpoints relacionados a lembretes de cobrança via WhatsApp")
public class NotificacaoController {

    private final GeracaoLembreteService geracaoLembreteService;

    @PostMapping("/enviar-automaticos")
    @Operation(summary = "Enviar lembretes automáticos manualmente",
               description = "Executa o processo de envio de lembretes de cobrança via WhatsApp. " +
                           "Normalmente este processo roda automaticamente às 09:00 via CRON Job. " +
                           "Este endpoint permite execução manual para testes.")
    public ResponseEntity<String> enviarLembretesAutomaticos() {
        geracaoLembreteService.executarGeracaoLembretes();
        return ResponseEntity.ok("Processo de envio de lembretes executado com sucesso. Verifique os logs para detalhes.");
    }

    @PostMapping("{idDespesa}/enviar-automaticos")
    @Operation(summary = "Enviar lembretes automáticos manualmente",
            description = "Executa o processo de envio de lembretes de cobrança via WhatsApp. " +
                    "Normalmente este processo roda automaticamente às 09:00 via CRON Job. " +
                    "Este endpoint permite execução manual para testes.")
    public ResponseEntity<String> enviarLembretesAutomaticos(@PathVariable UUID idDespesa) {
        geracaoLembreteService.executarGeracaoLembretes(idDespesa);
        return ResponseEntity.ok("Processo de envio de lembretes executado com sucesso. Verifique os logs para detalhes.");
    }
}

