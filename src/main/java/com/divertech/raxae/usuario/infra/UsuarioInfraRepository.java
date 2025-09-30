package com.divertech.raxae.usuario.infra;

import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Log4j2
@RequiredArgsConstructor
public class UsuarioInfraRepository implements UsuarioRepository {
    private final UsuarioSpringDataJpaRepository usuarioSpringDataRepository;

    @Override
    public Usuario salva(Usuario usuario) {
        log.info("[start] UsuarioInfraRepository - salva");
        Usuario salvo = usuarioSpringDataRepository.save(usuario);
        log.debug("[finish] UsuarioInfraRepository - salva");
        return salvo;
    }

    @Override
    public Optional<Usuario> buscaUsuarioPorEmail(String email) {
        log.info("[start] UsuarioInfraRepository - buscaUsuarioPorEmail");
        Optional<Usuario> usuario = usuarioSpringDataRepository.findByEmail(email);
        log.debug("[finish] UsuarioInfraRepository - buscaUsuarioPorEmail");
        return usuario;
    }

    @Override
    public Optional<Usuario> buscaUsuarioPorId(UUID id) {
        log.info("[start] UsuarioInfraRepository - buscaUsuarioPorId");
        Optional<Usuario> usuario = usuarioSpringDataRepository.findById(id);
        log.debug("[finish] UsuarioInfraRepository - buscaUsuarioPorId");
        return usuario;
    }
}
