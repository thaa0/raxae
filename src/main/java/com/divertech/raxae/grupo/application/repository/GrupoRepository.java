package com.divertech.raxae.grupo.application.repository;

import com.divertech.raxae.grupo.domain.Grupo;

import java.util.Optional;
import java.util.UUID;

public interface GrupoRepository {
    Grupo buscaGrupoPorId(UUID idDoGrupo);
    void apagaGrupo(Grupo grupo);
}
