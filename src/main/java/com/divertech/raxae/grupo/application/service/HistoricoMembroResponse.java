package com.divertech.raxae.grupo.application.service;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class HistoricoMembroResponse {
    private UUID grupoId;
    private UUID membroId;
    private BigDecimal totalDespesasRealizadas;
    private BigDecimal totalCobrancasRecebidas;
    private BigDecimal saldo;
    private List<ItemHistoricoResponse> historico;
}