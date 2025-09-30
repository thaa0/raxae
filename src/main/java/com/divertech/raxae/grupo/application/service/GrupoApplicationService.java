package com.divertech.raxae.grupo.application.service;

import com.divertech.raxae.auth.config.service.JwtService;
import com.divertech.raxae.grupo.application.controller.GrupoEditaRequest;
import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;
import com.divertech.raxae.grupo.application.controller.GrupoResponse;
import com.divertech.raxae.grupo.application.repository.GrupoRepository;
import com.divertech.raxae.grupo.domain.Grupo;
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
    private final JwtService jwtService;

    @Transactional
    public void deletarGrupo(UUID idDoGrupo, UUID idUsuarioAtual) {
        log.info("[start] GrupoApplicationService - deletarGrupo");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        possuiPermissaoDeAdmin(idUsuarioAtual, grupo);
        grupoRepository.apagaGrupo(grupo);
        log.debug("[finish] GrupoApplicationService - deletarGrupo");
    }

    @Override
    public GrupoResponse criaGrupo(GrupoNovoRequest grupoRequest) {
        log.info("[start] GrupoApplicationService - criaGrupo");
        Grupo grupo = new Grupo(grupoRequest);
        grupoRepository.salva(grupo);
        GrupoResponse grupoResponse = new GrupoResponse(grupo);
        log.debug("[finish] GrupoApplicationService - criaGrupo");
        return grupoResponse;
    }

    @Override
    public GrupoResponse getGrupoById(UUID idDoGrupo, UUID id) {
        log.info("[start] GrupoApplicationService - getGrupoById");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        GrupoResponse grupoResponse = new GrupoResponse(grupo);
        log.debug("[finish] GrupoApplicationService - getGrupoById");
        return grupoResponse;
    }

    @Override
    public void editarGrupo(UUID idDoGrupo, GrupoEditaRequest grupoEditaRequest, UUID id) {
        log.info("[start] GrupoApplicationService - editarGrupo");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        log.info("Puxando Info do User:");
        log.info(grupo.getAdminId() + "Id do Admin");
        log.info(id + "Id do User Atual");
        possuiPermissaoDeAdmin(id, grupo);
        grupoRepository.editarGrupo(idDoGrupo, grupoEditaRequest);
        log.debug("[finish] GrupoApplicationService - editarGrupo");
    }

    private static void possuiPermissaoDeAdmin(UUID idUsuarioAtual, Grupo grupo) {
        if (!grupo.getAdminId().equals(idUsuarioAtual)) {
            throw new AccessDeniedException("Usuário não autorizado para realizar alterações neste grupo.");
        }
    }
}