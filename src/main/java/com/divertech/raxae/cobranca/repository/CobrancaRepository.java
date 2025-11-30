package com.divertech.raxae.cobranca.repository;

import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.domain.StatusCobranca;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CobrancaRepository {
    List<Cobranca> salvarVarias(List<Cobranca> cobrancas);
    List<Cobranca> buscaPorIdDaDespesa(UUID despesaId);
    boolean existeCobrancaParaDespesaEMes(UUID despesaId, String mesReferencia);

    List<Cobranca> buscarPorStatusMesReferenciaEDataVencimento(StatusCobranca status, String mesReferencia, LocalDate dataVencimento);

    Cobranca buscarPorDespesaIdEUsuarioId(UUID despesaId, UUID usuarioId);

    Cobranca buscaPorId(UUID cobrancaId);
    List<Cobranca> findByDespesaGrupoIdAndUsuarioId(UUID grupoId, UUID usuarioId);
    
    List<Cobranca> buscarCobrancasPagasPorUsuarioEMes(UUID usuarioId, java.time.YearMonth mes);
    List<Cobranca> buscarTodasCobrancasPagasPorUsuario(UUID usuarioId);
}