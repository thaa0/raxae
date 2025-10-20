package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CobrancaInfraRepository implements CobrancaRepository {

    private final CobrancaSpringDataJPARepository jpaRepository;

    @Override
    public List<Cobranca> salvarVarias(List<Cobranca> cobrancas) {
        return jpaRepository.saveAll(cobrancas);
    }
    @Override
    public List<Cobranca> buscaPorIdDaDespesa(UUID despesaId) {
        return jpaRepository.findByDespesaId(despesaId);
    }
}