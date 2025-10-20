package com.divertech.raxae.cobranca.application.port.out;

import com.divertech.raxae.usuario.domain.Usuario;
import java.util.List;

public interface NotificacaoServicePort {

    void notificarMembros(List<Usuario> usuarios, String mensagem);

}