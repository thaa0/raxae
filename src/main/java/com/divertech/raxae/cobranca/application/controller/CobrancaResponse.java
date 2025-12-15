package com.divertech.raxae.cobranca.application.controller;

import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.domain.StatusCobranca;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CobrancaResponse {
    private UUID id;
    private UUID despesaId;
    private String despesaNome;
    private UUID grupoId;
    private String grupoNome;
    private String nome;
    private BigDecimal valor;
    private StatusCobranca status;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private LocalDateTime momentoCriacao;
    private String mesReferencia;

    public static CobrancaResponse from(Cobranca cobranca) {
        return CobrancaResponse.builder()
                .id(cobranca.getId())
                .despesaId(cobranca.getDespesa().getId())
                .despesaNome(cobranca.getDespesa().getNome())
                .grupoId(cobranca.getDespesa().getGrupo().getId())
                .grupoNome(cobranca.getDespesa().getGrupo().getNomeGrupo())
                .nome(cobranca.getUsuario().getNomeCompleto())
                .valor(cobranca.getValor())
                .status(cobranca.getStatus())
                .dataVencimento(cobranca.getDataVencimento())
                .dataPagamento(cobranca.getDataPagamento())
                .momentoCriacao(cobranca.getMomentoCriacao())
                .mesReferencia(cobranca.getMesReferencia())
                .build();
    }
}

