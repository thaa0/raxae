package com.divertech.raxae.plano.application.controller;

import com.divertech.raxae.plano.domain.Plano;
import com.divertech.raxae.plano.domain.TipoPlano;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class PlanoResponse {

    private UUID id;
    private TipoPlano tipo;
    private String descricao;
    private BigDecimal precoMensal;
    private int limiteGrupos;
    private int limiteMembrosPorGrupo;
    private int limiteDespesaPorGrupo;

    public PlanoResponse(Plano plano) {
        this.id = plano.getId();
        this.tipo = plano.getTipo();
        this.descricao = plano.getDescricao();
        this.precoMensal = plano.getPrecoMensal();
        this.limiteGrupos = plano.getLimiteGrupos();
        this.limiteMembrosPorGrupo = plano.getLimiteMembrosPorGrupo();
        this.limiteDespesaPorGrupo = plano.getLimiteDespesaPorGrupo();
    }
}