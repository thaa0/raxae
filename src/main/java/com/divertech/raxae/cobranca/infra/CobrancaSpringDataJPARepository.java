package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.domain.StatusCobranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

public interface CobrancaSpringDataJPARepository extends JpaRepository<Cobranca, UUID> {
    List<Cobranca> findByDespesaId(UUID despesaId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cobranca c " +
           "WHERE c.despesa.id = :despesaId AND c.mesReferencia = :mesReferencia")
    boolean existsByDespesaIdAndMesReferencia(@Param("despesaId") UUID despesaId,
                                               @Param("mesReferencia") String mesReferencia);

    @Query("SELECT c FROM Cobranca c " +
           "WHERE c.status = :status " +
           "AND c.mesReferencia = :mesReferencia " +
           "AND c.dataVencimento = :dataVencimento")
    List<Cobranca> findByStatusAndMesReferenciaAndDataVencimento(
            @Param("status") StatusCobranca status,
            @Param("mesReferencia") String mesReferencia,
            @Param("dataVencimento") LocalDate dataVencimento
    );
}