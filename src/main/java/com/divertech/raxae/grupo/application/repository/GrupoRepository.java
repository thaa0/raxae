package com.divertech.raxae.grupo.application.repository;

import com.divertech.raxae.grupo.application.controller.GrupoEditaRequest;
import com.divertech.raxae.grupo.domain.Grupo;

import java.util.Optional;
import java.util.UUID;

public interface GrupoRepository {
    Optional<Grupo> buscaGrupoPorId(UUID idDoGrupo);
    void apagaGrupo(Grupo grupo);
    void salva(Grupo grupo);
    void editarGrupo(UUID idDoGrupo, GrupoEditaRequest grupoEditaRequest);
}
