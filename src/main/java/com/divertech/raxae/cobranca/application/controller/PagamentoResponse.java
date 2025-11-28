package com.divertech.raxae.cobranca.application.controller;

import com.divertech.raxae.cobranca.domain.Pagamento;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PagamentoResponse {
    UUID pagamentoId;
    UUID cobrancaId;
    String status;

    public PagamentoResponse(Pagamento pagamento) {
        this.pagamentoId = pagamento.getId();
        this.cobrancaId = pagamento.getCobranca().getId();
        this.status = pagamento.getStatus().name();
    }
}
