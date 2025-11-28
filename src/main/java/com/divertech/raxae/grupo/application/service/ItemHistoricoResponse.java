package com.divertech.raxae.grupo.application.service;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ItemHistoricoResponse {
    private UUID id;
    private String tipo;
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime data;
    private String status;
}