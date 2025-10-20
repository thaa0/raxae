package com.divertech.raxae.cobranca.repository;

import com.divertech.raxae.cobranca.domain.Cobranca;
import java.util.List;

public interface CobrancaRepository {
    List<Cobranca> salvarVarias(List<Cobranca> cobrancas);
}