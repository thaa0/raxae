package com.divertech.raxae.usuario.application.controller;

import com.divertech.raxae.usuario.application.service.UsuarioService;
import com.divertech.raxae.usuario.domain.UsuarioRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/usuario")
@RequiredArgsConstructor
@Log4j2
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    void postNovoUsuario(@RequestBody @Valid UsuarioRequest usuarioNovo) {
        log.info("[start] UsuarioController - postNovoUsuario");
        usuarioService.cadastrarUsuario(usuarioNovo);
        log.debug("[finish] UsuarioController - postNovoUsuario");
    }
}
