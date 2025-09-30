package com.divertech.raxae.grupo.application.controller;

import com.divertech.raxae.grupo.application.service.GrupoService;
import com.divertech.raxae.usuario.domain.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/grupos")
@Log4j2
@RequiredArgsConstructor
public class GrupoController {
    private final GrupoService service;

    @PostMapping("/{idDoGrupo}/membros")
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionarMembro(
            @PathVariable UUID idDoGrupo,
            @Valid @RequestBody AdicionarMembroRequest adicionarMembroRequest,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        log.info("[start] GrupoController - adicionarMembro");
        service.adicionarMembro(idDoGrupo, adicionarMembroRequest, usuarioAtual.getId());
        log.info("[finish] GrupoController - adicionarMembro");
    }

    @DeleteMapping("/{idDoGrupo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarGrupo(
            @PathVariable UUID idDoGrupo,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        log.info("[start] GrupoController - deletarGrupo");
        service.deletarGrupo(idDoGrupo, usuarioAtual.getId());
        log.info("[finish] GrupoController - deletarGrupo");
    }
}