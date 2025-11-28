package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.domain.StatusCobranca;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
import com.divertech.raxae.handler.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Override
    public Cobranca buscarPorDespesaIdEUsuarioId(UUID despesaId, UUID usuarioId) {
        return jpaRepository.findByDespesaIdAndUsuarioId(despesaId, usuarioId);
    }

    @Override
    public Cobranca buscaPorId(UUID cobrancaId) {
        return jpaRepository.findById(cobrancaId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cobrança não encontrada"));
    }

}