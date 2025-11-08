package com.divertech.raxae.plano.application.service;

import com.divertech.raxae.plano.application.controller.AdesaoResponse;
import com.divertech.raxae.plano.application.controller.AtualizarPlanoRequest;
import com.divertech.raxae.plano.application.controller.PlanoResponse;
import com.divertech.raxae.usuario.domain.Usuario;

import java.util.List;
import java.util.UUID;


public interface PlanoService {

    List<PlanoResponse> listarPlanos();

    AdesaoResponse atualizarPlano(Usuario usuarioLogado, AtualizarPlanoRequest request);
}