package com.divertech.raxae.usuario.infra;

import com.divertech.raxae.grupo.application.repository.GrupoRepository;
import com.divertech.raxae.grupo.infra.GrupoSpringDataJPARepository;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Log4j2
@RequiredArgsConstructor
public class UsuarioInfraRepository implements UsuarioRepository {
    private final UsuarioSpringDataJpaRepository usuarioSpringDataRepository;
    private final GrupoRepository grupoRepository;

    @Override
    public void salva(Usuario usuario) {
        log.info("[start] UsuarioInfraRepository - salva");
        usuarioSpringDataRepository.save(usuario);
        log.debug("[finish] UsuarioInfraRepository - salva");
    }

    @Override
    public Usuario buscaUsuarioPorEmail(String email) {
        log.info("[start] UsuarioInfraRepository - buscaUsuarioPorEmail");
        Usuario usuario = usuarioSpringDataRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Usuário não encontrado para o e-mail: " + email));
        log.info("[finish] UsuarioInfraRepository - buscaUsuarioPorEmail");
        return usuario;
    }

    @Override
    public Usuario buscaUsuarioPorId(UUID id) {
        log.info("[start] UsuarioInfraRepository - buscaUsuarioPorId");
        Usuario usuario = usuarioSpringDataRepository.findById(id)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Usuário não encontrado com o ID: " + id));
        log.debug("[finish] UsuarioInfraRepository - buscaUsuarioPorId");
        return usuario;
    }

    @Override
    public long count() {
        log.info("[start] UsuarioInfraRepository - count");
        long total = usuarioSpringDataRepository.count();
        log.info("[finish] UsuarioInfraRepository - count");
        return total;
    }

    @Override
    public void saveAll(Iterable<Usuario> usuarios) {
        log.info("[start] UsuarioInfraRepository - saveAll");
        usuarioSpringDataRepository.saveAll(usuarios);
        log.info("[finish] UsuarioInfraRepository - saveAll");
    }

    @Override
    public int contaGruposDoUsuario(UUID id) {
        log.info("[start] UsuarioInfraRepository - contaGruposDoUsuario");
        int numeroDeGrupos = grupoRepository.buscaGruposPorUsuario(id).size();
        log.info("[finish] UsuarioInfraRepository - contaGruposDoUsuario");
        return numeroDeGrupos;
    }
}