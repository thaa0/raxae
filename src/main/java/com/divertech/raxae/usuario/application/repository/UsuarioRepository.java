package com.divertech.raxae.usuario.application.repository;

import com.divertech.raxae.usuario.domain.Usuario;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {
    Usuario salva(Usuario usuario);
    Optional<Usuario> buscaUsuarioPorEmail(String email);
    Optional<Usuario> buscaUsuarioPorId(UUID idUsuario);
}
