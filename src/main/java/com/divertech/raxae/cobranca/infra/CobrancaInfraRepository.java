package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.domain.StatusCobranca;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
    @Override
    public boolean existeCobrancaParaDespesaEMes(UUID despesaId, String mesReferencia) {
        return jpaRepository.existsByDespesaIdAndMesReferencia(despesaId, mesReferencia);
    }

    @Override
    public List<Cobranca> buscarPorStatusMesReferenciaEDataVencimento(StatusCobranca status, String mesReferencia, LocalDate dataVencimento) {
        return jpaRepository.findByStatusAndMesReferenciaAndDataVencimento(status, mesReferencia, dataVencimento);
    }
}