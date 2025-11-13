package com.divertech.raxae.cobranca.application.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DivisaoRequest {
    private UUID usuarioId;
    private BigDecimal valor;
    private BigDecimal percentual;
}
