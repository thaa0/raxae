package com.divertech.raxae.grupo.application.controller;

import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.grupo.domain.Membro;
import com.divertech.raxae.grupo.domain.StatusParticipacao;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@ToString
public class GrupoResponse {
    private UUID id;
    private String nomeGrupo;
    private String descricao;
    private String icone;
    private UUID adminId;
    private LocalDateTime dataCriacao;
    private Set<MembroResponse> membros;

    public GrupoResponse(Grupo grupo) {
        this.id = grupo.getId();
        this.nomeGrupo = grupo.getNomeGrupo();
        this.descricao = grupo.getDescricao();
        this.icone = grupo.getIcone();
        this.adminId = grupo.getAdminId();
        this.dataCriacao = grupo.getDataCriacao();
        this.membros = grupo.getMembros().stream()
                .map(MembroResponse::new)
                .collect(Collectors.toSet());
    }

    @Getter
    @ToString
    private static class MembroResponse {
        private UUID idMembro;
        private UUID idUsuario;
        private String nomeUsuario;
        private StatusParticipacao status;

        MembroResponse(Membro membro) {
            this.idMembro = membro.getId();
            this.idUsuario = membro.getUsuario().getId();
            this.nomeUsuario = membro.getUsuario().getNomeCompleto();
            this.status = membro.getStatus();
        }
    }
}