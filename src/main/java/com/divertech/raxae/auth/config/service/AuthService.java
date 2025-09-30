package com.divertech.raxae.auth.config.service;

import com.divertech.raxae.usuario.application.controller.LoginRequest;
import com.divertech.raxae.auth.domain.Token;
import com.divertech.raxae.usuario.domain.Usuario;
import jakarta.validation.Valid;

public interface AuthService {
    Token login(@Valid LoginRequest request);
    Usuario buscaCredencialPorUsuario(String usuario);
}
