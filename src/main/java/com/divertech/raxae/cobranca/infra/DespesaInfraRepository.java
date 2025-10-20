package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.repository.DespesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DespesaInfraRepository implements DespesaRepository {

    private final DespesaSpringDataJPARepository jpaRepository;

    @Override
    public Despesa salvar(Despesa despesa) {
        return jpaRepository.save(despesa);
    }
    @Override
    public Despesa buscaPorId(UUID id) {
        return jpaRepository.findById(id).orElse(null);
    }
}