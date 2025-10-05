package com.divertech.raxae.grupo.application.service;

import com.divertech.raxae.grupo.application.controller.GrupoEditaRequest;
import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;
import com.divertech.raxae.grupo.application.controller.GrupoResponse;

import java.util.UUID;

public interface GrupoService {
    void deletarGrupo(UUID idDoGrupo, UUID id);
    GrupoResponse criaGrupo(GrupoNovoRequest grupoRequest);
    GrupoResponse getGrupoById(UUID idDoGrupo, UUID id);
    void editarGrupo(UUID idDoGrupo, GrupoEditaRequest grupoEditaRequest, UUID id);
    void removerMembro(UUID idDoGrupo, UUID idDoMembro, UUID id);
    void adicionarMembro(UUID idGrupo, String emailNovoMembro);
}
