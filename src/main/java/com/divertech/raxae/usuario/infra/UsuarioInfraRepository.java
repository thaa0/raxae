package com.divertech.raxae.usuario.infra;

import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Log4j2
@RequiredArgsConstructor
public class UsuarioInfraRepository implements UsuarioRepository {
    private final UsuarioSpringDataJpaRepository usuarioSpringDataRepository;

    @Override
    public void salva(Usuario usuario) {
        log.info("[start] UsuarioInfraRepository - salva");
        usuarioSpringDataRepository.save(usuario);
        log.debug("[finish] UsuarioInfraRepository - salva");
    }

    public Usuario buscaUsuario(String email){
        log.info("[start] UsuarioInfraRepository - findByEmail");
        Usuario usuario = usuarioSpringDataRepository.findByEmail(email)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));
        log.debug("[finish] UsuarioInfraRepository - findByEmail");
        return usuario;
    }
}