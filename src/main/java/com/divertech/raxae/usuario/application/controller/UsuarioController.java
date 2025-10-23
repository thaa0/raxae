package com.divertech.raxae.usuario.application.controller;

import com.divertech.raxae.auth.config.service.AuthService;
import com.divertech.raxae.auth.domain.Token;
import com.divertech.raxae.usuario.application.service.UsuarioService;
import com.divertech.raxae.usuario.domain.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
@Log4j2
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final AuthService authService;

    @PostMapping("/cadastro")
    @ResponseStatus(code = HttpStatus.CREATED)
    void postNovoUsuario(@RequestBody @Valid UsuarioRequest usuarioNovo) {
        log.info("[start] UsuarioController - postNovoUsuario");
        usuarioService.cadastrarUsuario(usuarioNovo);
        log.debug("[finish] UsuarioController - postNovoUsuario");
    }

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Token login(@RequestBody @Valid LoginRequest request) {
        log.info("[start] UsuarioController - login");
        Token tokenResponse = authService.login(request);
        log.debug("[finish] UsuarioController - login");
        return tokenResponse;
    }

    //Get - Ver info por usuário, deve retornar InfoUsuarioResponse - Deve puxar as infos com base no usuário que está fazendo a requisição (token), os campos double EconomiaTotal;
    //    double totalPagoNoMes devem ser mockados, use variáveis enquanto nao temos os metodos prontos.
    @GetMapping("/info")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<InfoUsuarioResponse> getInfoUsuario(@AuthenticationPrincipal Usuario usuarioLogado) {
        log.info("[start] UsuarioController - getInfoUsuario");
        InfoUsuarioResponse infoUsuarioResponse = usuarioService.getInfoUsuario(usuarioLogado);
        log.debug("[finish] UsuarioController - getInfoUsuario");
        return ResponseEntity.ok(infoUsuarioResponse);
    }

}
