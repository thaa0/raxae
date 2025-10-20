package com.divertech.raxae.cobranca.repository;

import com.divertech.raxae.cobranca.domain.Despesa;
import java.util.UUID;

public interface DespesaRepository {
    Despesa salvar(Despesa despesa);
}