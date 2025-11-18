package com.divertech.raxae.cobranca.repository;

import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.domain.StatusDespesa;

import java.util.List;
import java.util.UUID;

public interface DespesaRepository {
    Despesa salvar(Despesa despesa);
    Despesa buscaPorId(UUID id);
    List<Despesa> buscarPorDiaVencimentoEStatus(Integer diaVencimento, StatusDespesa status);
}