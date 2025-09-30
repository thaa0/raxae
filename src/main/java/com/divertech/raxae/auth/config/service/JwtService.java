package com.divertech.raxae.auth.config.service;

import com.divertech.raxae.usuario.domain.Usuario;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface JwtService {
    String gerarToken(Usuario usuario);
    Optional<String> getUsuarioByBearerToken(String token);
    String gerarToken(Authentication authentication);
    Optional<String> getUsuario(String token);
}
