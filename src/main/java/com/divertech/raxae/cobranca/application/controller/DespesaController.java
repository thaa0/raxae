package com.divertech.raxae.cobranca.application.controller;

import com.divertech.raxae.cobranca.application.service.DespesaService;
import com.divertech.raxae.usuario.domain.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/grupos/{grupoId}/despesas")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaService despesaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DespesaResponse registrarDespesa(
            @PathVariable UUID grupoId,
            @Valid @RequestBody DespesaRequest request,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        
        return despesaService.registraDespesa(grupoId, request, usuarioAtual.getEmail());
    }
    
    @DeleteMapping("/{despesaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirDespesa(
            @PathVariable UUID grupoId,
            @PathVariable UUID despesaId,
            @AuthenticationPrincipal String emailUsuarioLogado) {
        
        despesaService.excluiDespesa(grupoId, despesaId, emailUsuarioLogado);
    }
}