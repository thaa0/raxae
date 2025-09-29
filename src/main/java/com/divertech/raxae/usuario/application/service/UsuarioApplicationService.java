package com.divertech.raxae.usuario.application.service;

import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import com.divertech.raxae.usuario.application.controller.UsuarioRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Log4j2
public class UsuarioApplicationService implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encriptador;

    @Override
    public void cadastrarUsuario(UsuarioRequest request) {
        log.info("[start] UsuarioApplicationService - cadastrarUsuario");
        Usuario usuario = new Usuario(request, encriptador);
        usuarioRepository.salva(usuario);
        log.debug("[finish] UsuarioApplicationService - cadastrarUsuario");
    }
}
