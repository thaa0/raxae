package com.divertech.raxae.plano.application.repository;

import com.divertech.raxae.plano.domain.Plano;
import com.divertech.raxae.plano.domain.TipoPlano;

import java.util.List;
import java.util.UUID;

public interface PlanoRepository {
    List<Plano> buscaTodos();
    Plano buscaPorId(UUID planoId);

    Plano buscaPorTipo(TipoPlano tipo);
}