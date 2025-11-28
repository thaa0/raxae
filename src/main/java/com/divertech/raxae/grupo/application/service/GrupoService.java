package com.divertech.raxae.grupo.application.service;

import com.divertech.raxae.grupo.application.controller.GrupoEditaRequest;
import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;
import com.divertech.raxae.grupo.application.controller.GrupoResponse;
import com.divertech.raxae.grupo.application.controller.MembroResponse;
import com.divertech.raxae.usuario.domain.Usuario;

import java.util.List;
import java.util.UUID;

public interface GrupoService {
    GrupoResponse criaGrupo(GrupoNovoRequest grupoRequest, Usuario usuarioAtual);
    void deletarGrupo(UUID idDoGrupo, UUID idUsuarioAtual);
    GrupoResponse getGrupoById(UUID idDoGrupo, UUID idUsuarioAtual);
    void editarGrupo(UUID idDoGrupo, GrupoEditaRequest grupoEditaRequest, UUID idUsuarioAtual);
    void removerMembro(UUID idDoGrupo, UUID idDoMembro, UUID idUsuarioAtual);
    void adicionarMembro(UUID idGrupo, Usuario usuario);
    String geraConvite(UUID idDoGrupo, Usuario usuarioAtual);
    List<GrupoResponse> getGruposPorUsuario(UUID id);
    List<MembroResponse> listarMembro(UUID idDoGrupo);
    void validaUsuarioAdmin(UUID idDoGrupo, UUID idUsuario);
}
