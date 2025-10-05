package com.divertech.raxae.grupo.application.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GrupoEditaRequest {
    private String nomeGrupo;
    private String descricao;
    private String icone;
}
