package com.divertech.raxae.grupo.service;

import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.grupo.repository.GrupoRepositorio;
import com.divertech.raxae.handler.RecursoNaoEncontradoException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class GrupoServico {
    private final GrupoRepositorio grupoRepositorio;

    public GrupoServico(GrupoRepositorio grupoRepositorio) {
        this.grupoRepositorio = grupoRepositorio;
    }

    @Transactional
    public void deletarGrupo(UUID idDoGrupo, UUID idUsuarioAtual) {
        Grupo grupo = grupoRepositorio.findById(idDoGrupo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Grupo não encontrado com id: " + idDoGrupo));

        if (!grupo.getAdminId().equals(idUsuarioAtual)) {
            throw new AccessDeniedException("Usuário não autorizado para deletar este grupo.");
        }

        grupoRepositorio.delete(grupo);
    }
}