package com.divertech.raxae.notificacao.domain.application.adapter;

import com.divertech.raxae.cobranca.application.port.out.NotificacaoServicePort;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class NotificacaoServiceAdapter implements NotificacaoServicePort {

    @Override
    public void notificarMembros(List<Usuario> usuarios, String mensagem) {
        log.info("[NOTIFICACAO MOCK] Notificando {} usuários: {}", usuarios.size(), mensagem);
    }
}