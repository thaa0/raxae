package com.divertech.raxae.grupo.application.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GrupoNovoRequest {
    private String nomeGrupo;
    private String descricao;
}
