package com.divertech.raxae.usuario.application.repository;

import com.divertech.raxae.usuario.domain.Usuario;

public interface UsuarioRepository {
    void salva(Usuario usuario);
    Usuario buscaUsuario(String email);
}
