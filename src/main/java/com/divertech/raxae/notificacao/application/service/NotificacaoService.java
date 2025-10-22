package com.divertech.raxae.notificacao.application.service;

import com.divertech.raxae.usuario.domain.Usuario;

import java.util.List;

public interface NotificacaoService {
    void notificarMembros(List<Usuario> usuarios, String mensagem);
}
