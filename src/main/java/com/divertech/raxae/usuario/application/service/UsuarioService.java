package com.divertech.raxae.usuario.application.service;

import com.divertech.raxae.usuario.application.controller.InfoUsuarioResponse;
import com.divertech.raxae.usuario.application.controller.UsuarioRequest;
import com.divertech.raxae.usuario.domain.Usuario;

public interface UsuarioService {
    void cadastrarUsuario(UsuarioRequest request);
    InfoUsuarioResponse getInfoUsuario(Usuario usuarioLogado);
}
