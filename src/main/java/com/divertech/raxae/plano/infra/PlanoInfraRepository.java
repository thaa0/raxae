package com.divertech.raxae.plano.infra;

import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.plano.application.repository.PlanoRepository;
import com.divertech.raxae.plano.domain.Plano;
import com.divertech.raxae.plano.domain.TipoPlano;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PlanoInfraRepository implements PlanoRepository {

    private final PlanoSpringDataJPARepository jpaRepository;

    @Override
    public List<Plano> buscaTodos() {
        return jpaRepository.findAll();
    }

    @Override
    public Plano buscaPorId(UUID planoId) {
        return jpaRepository.findById(planoId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Plano não encontrado!"));
    }

    @Override
    public Plano buscaPorTipo(TipoPlano tipo) {
        return jpaRepository.findByTipo(tipo)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Plano " + tipo + " não encontrado na base de dados."));
    }
}