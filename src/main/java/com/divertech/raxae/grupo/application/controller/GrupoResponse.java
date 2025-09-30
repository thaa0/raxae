package com.divertech.raxae.grupo.application.controller;

import com.divertech.raxae.grupo.domain.Grupo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

public class GrupoResponse {
    private String idGrupo;
    private String nomeGrupo;
    private String descricao;
    private String icone;
    private String idUserAdmin;

    public GrupoResponse(Grupo grupo) {
        this.idGrupo = grupo.getId().toString();
        this.nomeGrupo = grupo.getNomeGrupo();
        this.descricao = grupo.getDescricao();
        this.icone = grupo.getIcone();
        this.idUserAdmin = grupo.getAdminId().toString();
    }
}
