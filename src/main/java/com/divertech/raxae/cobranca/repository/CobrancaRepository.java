package com.divertech.raxae.cobranca.repository;

import com.divertech.raxae.cobranca.domain.Cobranca;
import java.util.List;
import java.util.UUID;

public interface CobrancaRepository {
    List<Cobranca> salvarVarias(List<Cobranca> cobrancas);
    List<Cobranca> buscaPorIdDaDespesa(UUID despesaId);
    boolean existeCobrancaParaDespesaEMes(UUID despesaId, String mesReferencia);
}