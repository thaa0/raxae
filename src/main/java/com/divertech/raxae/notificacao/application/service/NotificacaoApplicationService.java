package com.divertech.raxae.notificacao.application.service;

import com.divertech.raxae.usuario.domain.Usuario;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class NotificacaoApplicationService implements NotificacaoService {

    @Override
    public void notificarMembros(List<Usuario> usuarios, String mensagem) {
        log.info("[NOTIFICACAO] Notificando {} usuários: {}", usuarios.size(), mensagem);
    }
}