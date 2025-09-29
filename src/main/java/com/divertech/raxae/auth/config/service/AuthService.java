package com.divertech.raxae.auth.config.service;

import com.divertech.raxae.usuario.application.controller.LoginRequest;
import com.divertech.raxae.auth.domain.Token;
import jakarta.validation.Valid;

public interface AuthService {
    Token login(@Valid LoginRequest request);
    Credencial buscaCredencialPorUsuario(String usuario);
}
