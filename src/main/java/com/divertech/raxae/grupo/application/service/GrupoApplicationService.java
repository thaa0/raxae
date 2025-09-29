package com.divertech.raxae.grupo.application.service;

import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;
import com.divertech.raxae.grupo.application.repository.GrupoRepository;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.handler.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class GrupoApplicationService implements GrupoService {
    private final GrupoRepository grupoRepository;

    @Transactional
    public void deletarGrupo(UUID idDoGrupo, UUID idUsuarioAtual) {
        log.info("[start] GrupoApplicationService - deletarGrupo");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        possuiPermissaoDeAdmin(idUsuarioAtual, grupo);
        grupoRepository.apagaGrupo(grupo);
        log.debug("[finish] GrupoApplicationService - deletarGrupo");
    }

    @Override
    public void criaGrupo(GrupoNovoRequest grupoRequest, String token) {
        log.info("[start] GrupoApplicationService - criaGrupo");
        
        log.debug("[finish] GrupoApplicationService - criaGrupo");
    }

    private static void possuiPermissaoDeAdmin(UUID idUsuarioAtual, Grupo grupo) {
        if (!grupo.getAdminId().equals(idUsuarioAtual)) {
            throw new AccessDeniedException("Usuário não autorizado para deletar este grupo.");
        }
    }
}