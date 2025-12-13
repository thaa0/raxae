package com.divertech.raxae.cobranca.application.controller;

import com.divertech.raxae.cobranca.application.service.CobrancaService;
import com.divertech.raxae.cobranca.application.service.GeracaoAutomaticaCobrancaService;
import com.divertech.raxae.usuario.domain.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller para execução manual do processo de geração de cobranças
 * Útil para testes e execuções pontuais
 */
@RestController
@RequestMapping("/cobrancas")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Cobranças", description = "Endpoints relacionados a cobranças")
public class CobrancaController {

    private final GeracaoAutomaticaCobrancaService geracaoAutomaticaCobrancaService;
    private final CobrancaService cobrancaService;

    @PostMapping("/gerar-automaticas")
    @Operation(summary = "Gerar cobranças automáticas manualmente",
               description = "Executa o processo de geração automática de cobranças para despesas recorrentes. " +
                           "Normalmente este processo roda automaticamente às 05:00 via CRON Job.")
    public ResponseEntity<String> gerarCobrancasAutomaticas() {
        geracaoAutomaticaCobrancaService.executarGeracaoAutomatica();
        return ResponseEntity.ok("Processo de geração automática de cobranças executado com sucesso. Verifique os logs para detalhes.");
    }

    @GetMapping("/minhas-cobrancas")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar cobranças do usuário logado",
               description = "Retorna todas as cobranças do usuário autenticado, ordenadas por data de vencimento (mais recentes primeiro).")
    public ResponseEntity<List<CobrancaResponse>> listarMinhasCobrancas(
            @AuthenticationPrincipal Usuario usuarioLogado) {
        log.info("[start] CobrancaController - listarMinhasCobrancas - usuarioId: {}", usuarioLogado.getId());
        List<CobrancaResponse> cobrancas = cobrancaService.listarCobrancasPorUsuario(usuarioLogado.getId());
        log.info("[finish] CobrancaController - listarMinhasCobrancas - {} cobranças retornadas", cobrancas.size());
        return ResponseEntity.ok(cobrancas);
    }

    @GetMapping("/grupo/{grupoId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todas as cobranças de um grupo (somente admin)",
               description = "Retorna todas as cobranças de um grupo com todos os status. " +
                           "Este endpoint é acessível apenas para administradores do grupo.")
    public ResponseEntity<List<CobrancaResponse>> listarCobrancasPorGrupo(
            @PathVariable UUID grupoId,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        log.info("[start] CobrancaController - listarCobrancasPorGrupo - grupoId: {}, usuarioId: {}", grupoId, usuarioLogado.getId());
        List<CobrancaResponse> cobrancas = cobrancaService.listarCobrancasPorGrupo(grupoId, usuarioLogado.getId());
        log.info("[finish] CobrancaController - listarCobrancasPorGrupo - {} cobranças retornadas", cobrancas.size());
        return ResponseEntity.ok(cobrancas);
    }


}

