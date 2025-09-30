package com.divertech.raxae.grupo.application.service;

import com.divertech.raxae.grupo.application.controller.AdicionarMembroRequest;
import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;
import com.divertech.raxae.grupo.domain.Grupo;
import java.util.Optional;
import java.util.UUID;

public interface GrupoService {
    void criaGrupo(GrupoNovoRequest grupoRequest);
    void deletarGrupo(UUID idDoGrupo, UUID idUsuarioAtual);
    Optional<Grupo> buscaGrupoPorId(UUID idDoGrupo);
    void adicionarMembro(UUID idDoGrupo, AdicionarMembroRequest adicionarMembroRequest, UUID idAdmin);
}