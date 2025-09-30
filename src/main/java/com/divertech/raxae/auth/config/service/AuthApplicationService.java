package com.divertech.raxae.auth.config.service;

import com.divertech.raxae.usuario.application.controller.LoginRequest;
import com.divertech.raxae.auth.domain.Token;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthApplicationService implements AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final @Lazy AuthenticationManager authenticationManager;

    @Override
    public Token login(LoginRequest request) {
        log.info("[start] AuthApplicationService - login");
        autentica(request);

        Usuario usuario = usuarioRepository.buscaUsuarioPorEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.gerarToken(usuario);
        log.debug("[finish] AuthApplicationService - login");

        return new Token("Bearer", token, usuario.getId());
    }

    @Override
    public Usuario buscaCredencialPorUsuario(String email) {
        log.info("[start] AuthApplicationService - buscaCredencialPorUsuario");

        Usuario usuario = usuarioRepository.buscaUsuarioPorEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        log.debug("[finish] AuthApplicationService - buscaCredencialPorUsuario");
        return usuario;
    }

    private void autentica(LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha());
            authenticationManager.authenticate(authToken);
        } catch (Exception e) {
            throw new RuntimeException("Falha na autenticação", e);
        }
    }
}
