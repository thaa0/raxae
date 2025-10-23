package com.divertech.raxae.grupo.application.controller;

import com.divertech.raxae.grupo.application.service.GrupoApplicationService;
import com.divertech.raxae.grupo.application.service.GrupoService;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.divertech.raxae.handler.APIException.build;

@RestController
@RequestMapping("/v1/grupo")
@RequiredArgsConstructor
@Log4j2
public class GrupoController {
    private final GrupoService grupoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GrupoResponse> criaGrupo(@RequestBody GrupoNovoRequest grupoNovoRequest, @AuthenticationPrincipal Usuario usuarioAtual){
        log.info("[start] GrupoController - criaGrupo");
        GrupoResponse grupoResponse = grupoService.criaGrupo(grupoNovoRequest,usuarioAtual);
        log.debug("[finish] GrupoController - criaGrupo");
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoResponse);
    }

    @DeleteMapping("/{idDoGrupo}")
    public ResponseEntity<Void> deletarGrupo(@PathVariable UUID idDoGrupo,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        log.info("[start] GrupoController - deletarGrupo");
        verificaUsuarioAuth(usuarioAtual);
        grupoService.deletarGrupo(idDoGrupo, usuarioAtual.getId());
        log.debug("[finish] GrupoController - deletarGrupo");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idDoGrupo}")
    public ResponseEntity<GrupoResponse> getGrupoById(@PathVariable UUID idDoGrupo,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        log.info("[start] GrupoController - getGrupoById");
        verificaUsuarioAuth(usuarioAtual);
        GrupoResponse grupoResponse = grupoService.getGrupoById(idDoGrupo, usuarioAtual.getId());
        log.debug("[finish] GrupoController - getGrupoById");
        return ResponseEntity.ok(grupoResponse);
    }

    @PatchMapping("/{idDoGrupo}")
    public ResponseEntity<GrupoResponse> editarGrupo(@PathVariable UUID idDoGrupo,
            @RequestBody GrupoEditaRequest grupoEditaRequest,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        log.info("[start] GrupoController - editarGrupo");
        verificaUsuarioAuth(usuarioAtual);
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
        verificaUsuarioAuth(usuarioAtual);
        grupoService.removerMembro(idDoGrupo, idDoMembro, usuarioAtual.getId());
        log.debug("[finish] GrupoController - removerMembro");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idDoGrupo}/convite")
    @ResponseStatus(HttpStatus.OK)
    public String geraConvite(@PathVariable UUID idDoGrupo, @AuthenticationPrincipal Usuario usuarioAtual){
        log.info("[start] GrupoController - geraConvite");
        verificaUsuarioAuth(usuarioAtual);
        log.debug("[finish] GrupoController - geraConvite");
        return grupoService.geraConvite(idDoGrupo, usuarioAtual);
    }

    @GetMapping("/{idDoGrupo}/join")
    @ResponseStatus(HttpStatus.OK)
    public void joinGrupo(@PathVariable UUID idDoGrupo, @AuthenticationPrincipal Usuario usuarioAtual){
        log.info("[start] GrupoController - geraConvite");
        verificaUsuarioAuth(usuarioAtual);
        grupoService.adicionarMembro(idDoGrupo, usuarioAtual);
        log.debug("[finish] GrupoController - geraConvite");
    }

    @GetMapping("/meus-grupos")
    public ResponseEntity<List<GrupoResponse>> getGruposPorUsuario(@AuthenticationPrincipal Usuario usuarioAtual) {
        log.info("[start] GrupoController - getGruposPorUsuario");
        verificaUsuarioAuth(usuarioAtual);
        List<GrupoResponse> grupos = grupoService.getGruposPorUsuario(usuarioAtual.getId());
        log.debug("[finish] GrupoController - getGruposPorUsuario");
        return ResponseEntity.ok(grupos);
    }

    @GetMapping("/{idDoGrupo}/membros")
    @ResponseStatus(HttpStatus.OK)
    public List<MembroResponse> listarMembrosPorGrupo(@PathVariable UUID idDoGrupo){
       log.info("[start] GrupoController - listarMembrosPorGrupo");
       return grupoService.listarMembro(idDoGrupo);
    }

    private static void verificaUsuarioAuth(Usuario usuarioAtual) {
        if (usuarioAtual == null) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuario atual não autenticado");
        }
    }
}