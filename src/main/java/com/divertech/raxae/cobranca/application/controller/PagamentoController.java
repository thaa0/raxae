package com.divertech.raxae.cobranca.application.controller;

import com.divertech.raxae.cobranca.application.service.PagamentoService;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/v1/expenses")
@RequiredArgsConstructor
@Log4j2
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping(value = "/{expenseId}/pay", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public PagamentoResponse registrarPagamento(
            @PathVariable UUID expenseId,
            @RequestParam("comprovante") MultipartFile comprovante,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        
        log.info("[start] PagamentoController - registrarPagamento para despesa: {}", expenseId);
        log.info("[finish] PagamentoController - registrarPagamento");
        return pagamentoService.registrarPagamentoComComprovante(expenseId, comprovante, usuarioAtual.getEmail());
    }

    @GetMapping("/{cobrancaId}/comprovante")
    public ResponseEntity<byte[]> buscarComprovante(
            @PathVariable UUID cobrancaId,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        
        log.info("[start] PagamentoController - buscarComprovante para a cobranca: {}", cobrancaId);
        byte[] comprovante = pagamentoService.buscarComprovantePorCobrancaId(cobrancaId, usuarioAtual.getEmail());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(comprovante.length);
        headers.setContentDispositionFormData("attachment", "comprovante.jpg");
        
        log.info("[finish] PagamentoController - buscarComprovante");
        return ResponseEntity.ok()
                .headers(headers)
                .body(comprovante);
    }

}

