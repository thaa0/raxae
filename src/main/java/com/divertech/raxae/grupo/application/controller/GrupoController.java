package com.divertech.raxae.grupo.application.controller;

import com.divertech.raxae.grupo.application.service.BuscarHistoricoMembroService;
import com.divertech.raxae.grupo.application.service.GrupoService;
import com.divertech.raxae.grupo.application.service.HistoricoMembroResponse;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.domain.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/grupo")
@RequiredArgsConstructor
@Log4j2
public class GrupoController {
    private final GrupoService grupoService;
    private final BuscarHistoricoMembroService buscarHistoricoMembroService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GrupoResponse> criaGrupo(
            @Parameter(
                    description = "Dados do grupo em JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GrupoNovoRequest.class)
                    )
            )
            @RequestPart("grupo")
            String grupoJson,
            @Parameter(
                    description = "Ícone do grupo",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("icone")
            MultipartFile icone,
            @AuthenticationPrincipal Usuario usuarioAtual
    ){
        log.info("[start] GrupoController - criaGrupo");
        GrupoNovoRequest grupoNovoRequest;
        try {
            grupoNovoRequest = new ObjectMapper()
                    .readValue(grupoJson, GrupoNovoRequest.class);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "JSON do grupo inválido", e
            );
        }
        GrupoResponse grupoResponse = grupoService.criaGrupo(grupoNovoRequest,icone,usuarioAtual);
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

    @GetMapping("/{idDoGrupo}/historico/{idDoMembro}")
    public ResponseEntity<HistoricoMembroResponse> buscarHistoricoMembro(
            @PathVariable UUID idDoGrupo,
            @PathVariable UUID idDoMembro,
            @AuthenticationPrincipal Usuario usuarioAtual) {
        log.info("[start] GrupoController - buscarHistoricoMembro");
        verificaUsuarioAuth(usuarioAtual);
        
        grupoService.validaUsuarioAdmin(idDoGrupo, usuarioAtual.getId());

        var response = buscarHistoricoMembroService.executar(idDoGrupo, idDoMembro);
        log.debug("[finish] GrupoController - buscarHistoricoMembro");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{idDoGrupo}/icone")
    public ResponseEntity<byte[]> buscarIcone(
            @PathVariable UUID idDoGrupo) {
        log.info("[start] GrupoController - buscarComprovante para a cobranca: {}", idDoGrupo);
        byte[] icone = grupoService.buscarIconePorGrupoId(idDoGrupo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(icone.length);
        headers.setContentDispositionFormData("attachment", "icone.jpg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(icone);
    }

    private static void verificaUsuarioAuth(Usuario usuarioAtual) {
        if (usuarioAtual == null) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuario atual não autenticado");
        }
    }
}