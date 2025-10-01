package com.divertech.raxae.grupo.application.controller;

import com.divertech.raxae.grupo.application.service.GrupoApplicationService;
import com.divertech.raxae.grupo.application.service.GrupoService;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/grupo")
@RequiredArgsConstructor
@Log4j2
public class GrupoController {
    private final GrupoService grupoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GrupoResponse> criaGrupo(@RequestBody GrupoNovoRequest grupoNovoRequest){
        log.info("[start] GrupoController - criaGrupo");
        GrupoResponse grupoResponse = grupoService.criaGrupo(grupoNovoRequest);
        log.debug("[finish] GrupoController - criaGrupo");
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoResponse);
    }

    @DeleteMapping("/{idDoGrupo}")
    public ResponseEntity<Void> deletarGrupo(@PathVariable UUID idDoGrupo,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        log.info("[start] GrupoController - deletarGrupo");
        if (usuarioAtual == null) {
            return ResponseEntity.status(401).build();
        }
        grupoService.deletarGrupo(idDoGrupo, usuarioAtual.getId());
        log.debug("[finish] GrupoController - deletarGrupo");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idDoGrupo}")
    public ResponseEntity<GrupoResponse> getGrupoById(@PathVariable UUID idDoGrupo,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        log.info("[start] GrupoController - getGrupoById");
        if (usuarioAtual == null) {
            return ResponseEntity.status(401).build();
        }
        GrupoResponse grupoResponse = grupoService.getGrupoById(idDoGrupo, usuarioAtual.getId());
        log.debug("[finish] GrupoController - getGrupoById");
        return ResponseEntity.ok(grupoResponse);
    }

    @PatchMapping("/{idDoGrupo}")
    public ResponseEntity<GrupoResponse> editarGrupo(@PathVariable UUID idDoGrupo,
            @RequestBody GrupoEditaRequest grupoEditaRequest,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        log.info("[start] GrupoController - editarGrupo");
        if (usuarioAtual == null) {
            return ResponseEntity.status(401).build();
        }
        grupoService.editarGrupo(idDoGrupo, grupoEditaRequest, usuarioAtual.getId());
        GrupoResponse grupoResponse = grupoService.getGrupoById(idDoGrupo, usuarioAtual.getId());
        log.debug("[finish] GrupoController - editarGrupo");
        return ResponseEntity.ok(grupoResponse);
    }

    @PatchMapping("/{idDoGrupo}/membro/{idDoMembro}/remover")
    public ResponseEntity<Void> removerMembro(@PathVariable UUID idDoGrupo,
            @PathVariable UUID idDoMembro,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        log.info("[start] GrupoController - removerMembro");
        if (usuarioAtual == null) {
            return ResponseEntity.status(401).build();
        }
        grupoService.removerMembro(idDoGrupo, idDoMembro, usuarioAtual.getId());
        log.debug("[finish] GrupoController - removerMembro");
        return ResponseEntity.noContent().build();
    }
}