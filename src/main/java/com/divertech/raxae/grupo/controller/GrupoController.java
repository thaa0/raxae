package com.divertech.raxae.grupo.controller;

import com.divertech.raxae.grupo.service.GrupoServico;
import com.divertech.raxae.usuario.domain.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/grupos")
public class GrupoController {
    private final GrupoServico grupoServico;

    public GrupoController(GrupoServico grupoServico) {
        this.grupoServico = grupoServico;
    }

    @DeleteMapping("/{idDoGrupo}")
    public ResponseEntity<Void> deletarGrupo(
            @PathVariable UUID idDoGrupo,
            @AuthenticationPrincipal Usuario usuarioAtual) {

        if (usuarioAtual == null) {
            return ResponseEntity.status(401).build();
        }

        grupoServico.deletarGrupo(idDoGrupo, usuarioAtual.getId());

        return ResponseEntity.noContent().build();
    }
}