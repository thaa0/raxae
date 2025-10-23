package com.divertech.raxae.usuario.application.repository;

import com.divertech.raxae.usuario.domain.Usuario;
import java.util.UUID;

public interface UsuarioRepository {
    void salva(Usuario usuario);
    Usuario buscaUsuarioPorEmail(String email);
    Usuario buscaUsuarioPorId(UUID id);
    long count();
    void saveAll(Iterable<Usuario> usuarios);
    int contaGruposDoUsuario(UUID id);
}