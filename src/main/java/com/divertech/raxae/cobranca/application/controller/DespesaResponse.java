package com.divertech.raxae.cobranca.application.controller;

import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.domain.TipoDivisao;
import com.divertech.raxae.cobranca.domain.TipoRecorrencia;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class DespesaResponse {

    private UUID id;
    private String nome;
    private BigDecimal valor;
    private TipoRecorrencia tipoRecorrencia;
    private TipoDivisao tipoDivisao;
    private LocalDateTime momentoCriacao;
    private UUID grupoId;
    private UUID adminId;

    public DespesaResponse(Despesa despesa) {
        this.id = despesa.getId();
        this.nome = despesa.getNome();
        this.valor = despesa.getValor();
        this.tipoRecorrencia = despesa.getTipoRecorrencia();
        this.tipoDivisao = despesa.getTipoDivisao();
        this.momentoCriacao = despesa.getMomentoCriacao();
        
        this.grupoId = despesa.getGrupo().getId(); 
        this.adminId = despesa.getCriadoPor().getId();
    }
}