package com.divertech.raxae.cobranca.repository;

import com.divertech.raxae.cobranca.domain.Pagamento;

import java.util.UUID;

public interface PagamentoRepository {
    Pagamento salvar(Pagamento pagamento);
    Pagamento buscarPorCobrancaId(UUID cobrancaId);
    Pagamento buscarPorId(UUID pagamentoId);
}

