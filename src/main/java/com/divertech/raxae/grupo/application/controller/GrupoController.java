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
    public void criaGrupo(@RequestBody GrupoNovoRequest grupoNovoRequest,
                          @RequestHeader(name = "Authorization", required = true) String token){
        log.info("[start] GrupoController - criaGrupo");
        grupoService.criaGrupo(grupoNovoRequest, token);
        log.debug("[finish] GrupoController - criaGrupo");
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
}