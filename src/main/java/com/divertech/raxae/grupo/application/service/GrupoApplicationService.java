package com.divertech.raxae.grupo.application.service;

import com.divertech.raxae.grupo.application.controller.GrupoEditaRequest;
import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;
import com.divertech.raxae.grupo.application.controller.GrupoResponse;
import com.divertech.raxae.grupo.application.controller.MembroResponse;
import com.divertech.raxae.grupo.application.repository.GrupoRepository;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class GrupoApplicationService implements GrupoService {

    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    @Value("${app.base-url:http://localhost:8080/raxae/api/v1/grupo/}")
    private String baseUrl;

    @Override
    @Transactional
    public GrupoResponse criaGrupo(GrupoNovoRequest grupoRequest, Usuario usuarioAtual) {
        log.info("[start] GrupoApplicationService - criaGrupo");
        Usuario admin = usuarioRepository.buscaUsuarioPorId(usuarioAtual.getId());
        Grupo grupo = new Grupo(grupoRequest);
        grupo.setAdministrador(admin);
        
        grupoRepository.salva(grupo);
        
        grupo.adicionaNovoMembro(admin);

        log.debug("[finish] GrupoApplicationService - criaGrupo");
        return new GrupoResponse(grupo);
    }

    @Override
    @Transactional
    public void deletarGrupo(UUID idDoGrupo, UUID idUsuarioAtual) {
        log.info("[start] GrupoApplicationService - deletarGrupo");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        possuiPermissaoDeAdmin(idUsuarioAtual, grupo);
        grupoRepository.apagaGrupo(grupo);
        log.debug("[finish] GrupoApplicationService - deletarGrupo");
    }

    @Override
    public GrupoResponse getGrupoById(UUID idDoGrupo, UUID idUsuarioAtual) {
        log.info("[start] GrupoApplicationService - getGrupoById");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        return new GrupoResponse(grupo);
    }

    @Override
    @Transactional
    public void editarGrupo(UUID idDoGrupo, GrupoEditaRequest grupoEditaRequest, UUID idUsuarioAtual) {
        log.info("[start] GrupoApplicationService - editarGrupo");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        possuiPermissaoDeAdmin(idUsuarioAtual, grupo);
        grupo.atualizaInformacoes(grupoEditaRequest);
        
        grupoRepository.salva(grupo);
        log.debug("[finish] GrupoApplicationService - editarGrupo");
    }

    @Override
    @Transactional
    public void removerMembro(UUID idDoGrupo, UUID idDoMembro, UUID idUsuarioAtual) {
        log.info("[start] GrupoApplicationService - removerMembro");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        possuiPermissaoDeAdmin(idUsuarioAtual, grupo);
        grupo.removeMembro(idDoMembro);
        

        log.debug("[finish] GrupoApplicationService - removerMembro");
    }

    @Override
    @Transactional
    public void adicionarMembro(UUID idGrupo, Usuario usuario) {
        log.info("[start] GrupoApplicationService - adicionarMembro");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idGrupo);

        verificaSeMembroJaEstaNoGrupo(usuario, grupo);
                
        if (grupo.getAdminId().equals(usuario.getId())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "O administrador já é membro do grupo.");
        }

        
        grupo.adicionaNovoMembro(usuario);
        
        log.info("[finish] GrupoApplicationService - adicionarMembro");
    }

    private void verificaSeMembroJaEstaNoGrupo(Usuario usuario, Grupo grupo) {
        if(grupo.buscaMembro(usuario) != null){
            throw APIException.build(HttpStatus.CONFLICT, "Usuario já esta no grupo!");
        }
    }

    @Override
    public String geraConvite(UUID idGrupo, Usuario usuarioAtual) {
        Grupo grupo = grupoRepository.buscaGrupoPorId(idGrupo);
        possuiPermissaoDeAdmin(usuarioAtual.getId(), grupo);
        return baseUrl + idGrupo + "/join";
    }

    @Override
    public List<GrupoResponse> getGruposPorUsuario(UUID id) {
        log.info("[start] GrupoApplicationService - getGruposPorUsuario");
        List<Grupo> grupos = grupoRepository.buscaGruposPorUsuario(id);
        log.debug("[finish] GrupoApplicationService - getGruposPorUsuario");
        return grupos.stream().map(GrupoResponse::new).toList();
    }

    @Override
    public List<MembroResponse> listarMembro(UUID idDoGrupo) {
        log.info("[start] GrupoApplicationService - listarMembro");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        List<MembroResponse> lista = grupo.getMembros().stream()
                .map(membro -> new MembroResponse(membro.getUsuario().getNomeCompleto(), membro.getStatus()))
                .toList();
        log.debug("[finish] GrupoApplicationService - listarMembro");
        return lista;
    }

    private void possuiPermissaoDeAdmin(UUID idUsuarioAtual, Grupo grupo) {
        if (!grupo.getAdminId().equals(idUsuarioAtual)) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuário não autorizado para realizar alterações neste grupo.");
        }
    }
}