package com.divertech.raxae.grupo.application.service;

import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;

import java.util.UUID;

public interface GrupoService {
    void deletarGrupo(UUID idDoGrupo, UUID id);
    void criaGrupo(GrupoNovoRequest grupoRequest);
}
