package com.divertech.raxae.grupo.application.service;

import com.divertech.raxae.auth.config.service.JwtService;
import com.divertech.raxae.grupo.application.controller.GrupoEditaRequest;
import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;
import com.divertech.raxae.grupo.application.controller.GrupoResponse;
import com.divertech.raxae.grupo.application.repository.GrupoRepository;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class GrupoApplicationService implements GrupoService {

    private final GrupoRepository grupoRepository;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public GrupoResponse criaGrupo(GrupoNovoRequest grupoRequest) {
        log.info("[start] GrupoApplicationService - criaGrupo");
        Usuario admin = usuarioRepository.buscaUsuarioPorId(UUID.fromString(grupoRequest.getIdUserAdmin()));
        Grupo grupo = new Grupo(grupoRequest);
        grupo.setAdministrador(admin);
        grupoRepository.salva(grupo);
        log.debug("[finish] GrupoApplicationService - criaGrupo");
        return new GrupoResponse(grupo);
    }

    @Transactional
    public void deletarGrupo(UUID idDoGrupo, UUID idUsuarioAtual) {
        log.info("[start] GrupoApplicationService - deletarGrupo");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        possuiPermissaoDeAdmin(idUsuarioAtual, grupo);
        grupoRepository.apagaGrupo(grupo);
        log.debug("[finish] GrupoApplicationService - deletarGrupo");
    }

    @Override
    public GrupoResponse getGrupoById(UUID idDoGrupo, UUID id) {
        log.info("[start] GrupoApplicationService - getGrupoById");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        return new GrupoResponse(grupo);
    }

    @Override
    @Transactional
    public void editarGrupo(UUID idDoGrupo, GrupoEditaRequest grupoEditaRequest, UUID id) {
        log.info("[start] GrupoApplicationService - editarGrupo");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        possuiPermissaoDeAdmin(id, grupo);
        grupo.atualizaInformacoes(grupoEditaRequest); 
        grupoRepository.salva(grupo); 
        log.debug("[finish] GrupoApplicationService - editarGrupo");
    }

    @Override
    @Transactional
    public void removerMembro(UUID idDoGrupo, UUID idDoMembro, UUID id) {
        log.info("[start] GrupoApplicationService - removerMembro");
        Grupo grupo = grupoRepository.buscaGrupoPorId(idDoGrupo);
        possuiPermissaoDeAdmin(id, grupo);
        grupo.removeMembro(idDoMembro);
        grupoRepository.salva(grupo);
        log.debug("[finish] GrupoApplicationService - removerMembro");
    }
    
    @Override
    @Transactional
    public void adicionarMembro(UUID idGrupo, String emailNovoMembro) {
        log.info("[start] GrupoApplicationService - adicionarMembro");
        
        Grupo grupo = grupoRepository.buscaGrupoPorId(idGrupo);
        
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!grupo.isAdmin(adminEmail)) {
            throw APIException.build(HttpStatus.FORBIDDEN, "Apenas o administrador pode adicionar novos membros.");
        }

        Usuario usuarioParaAdicionar = usuarioRepository.buscaUsuarioPorEmail(emailNovoMembro.toLowerCase());
        
        if (grupo.jaEhMembro(usuarioParaAdicionar)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Este usuário já é membro do grupo.");
        }

        grupo.adicionaNovoMembro(usuarioParaAdicionar);

        grupoRepository.salva(grupo);
        
        log.info("Simulando envio de notificação para {}", emailNovoMembro);

        log.info("[finish] GrupoApplicationService - adicionarMembro");
    }
    
    private void possuiPermissaoDeAdmin(UUID idUsuarioAtual, Grupo grupo) {
        if (!grupo.getAdminId().equals(idUsuarioAtual)) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuário não autorizado para realizar alterações neste grupo.");
        }
    }
}