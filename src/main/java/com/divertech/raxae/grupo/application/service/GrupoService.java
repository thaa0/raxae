package com.divertech.raxae.grupo.application.service;

import com.divertech.raxae.grupo.application.controller.GrupoEditaRequest;
import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;
import com.divertech.raxae.grupo.application.controller.GrupoResponse;
import java.util.UUID;

public interface GrupoService {
    GrupoResponse criaGrupo(GrupoNovoRequest grupoRequest);
    void deletarGrupo(UUID idDoGrupo, UUID idUsuarioAtual);
    GrupoResponse getGrupoById(UUID idDoGrupo, UUID idUsuarioAtual);
    void editarGrupo(UUID idDoGrupo, GrupoEditaRequest grupoEditaRequest, UUID idUsuarioAtual);
    void removerMembro(UUID idDoGrupo, UUID idDoMembro, UUID idUsuarioAtual);
    void adicionarMembro(UUID idGrupo, String emailNovoMembro);
}
