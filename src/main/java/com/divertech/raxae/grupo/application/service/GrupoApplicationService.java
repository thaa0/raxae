package com.divertech.raxae.grupo.application.service;

import com.divertech.raxae.grupo.application.controller.AdicionarMembroRequest;
import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;
import com.divertech.raxae.grupo.application.repository.GrupoRepository;
import com.divertech.raxae.grupo.application.repository.MembroRepository;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.grupo.domain.Membro;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class GrupoApplicationService implements GrupoService {
    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MembroRepository membroRepository;

    @Override
    public void criaGrupo(GrupoNovoRequest grupoRequest) {
        log.info("[start] GrupoApplicationService - criaGrupo");
        Grupo grupo = new Grupo(grupoRequest);
        grupoRepository.salva(grupo);
        log.info("[finish] GrupoApplicationService - criaGrupo");
    }

    @Override
    public void deletarGrupo(UUID idDoGrupo, UUID idUsuarioAtual) {
        log.info("[start] GrupoApplicationService - deletarGrupo");
        Grupo grupo = buscaEValidaAdminDoGrupo(idDoGrupo, idUsuarioAtual);
        grupo.desativa();
        grupoRepository.salva(grupo);
        log.info("[finish] GrupoApplicationService - deletarGrupo");
    }

    @Override
    public Optional<Grupo> buscaGrupoPorId(UUID idDoGrupo) {
        return grupoRepository.buscaGrupoPorId(idDoGrupo);
    }

    @Override
    public void adicionarMembro(UUID idDoGrupo, AdicionarMembroRequest adicionarMembroRequest, UUID idAdmin) {
        log.info("[start] GrupoApplicationService - adicionarMembro");

        Grupo grupo = buscaEValidaAdminDoGrupo(idDoGrupo, idAdmin);

        Usuario usuarioConvidado = usuarioRepository.buscaUsuarioPorEmail(adicionarMembroRequest.email())
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Usuário com o e-mail informado não foi encontrado."));

        if (membroRepository.existeMembroNoGrupo(idDoGrupo, usuarioConvidado.getId())) {
            throw APIException.build(HttpStatus.CONFLICT, "Este usuário já é um membro ou possui um convite pendente para este grupo.");
        }

        Membro novoMembro = new Membro(grupo, usuarioConvidado);
        membroRepository.salva(novoMembro);

        log.info("[finish] GrupoApplicationService - adicionarMembro");
    }

    private Grupo buscaEValidaAdminDoGrupo(UUID idDoGrupo, UUID idAdmin) {
        return this.buscaGrupoPorId(idDoGrupo)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Grupo não encontrado."))
            .validaAdmin(idAdmin);
    }
}
