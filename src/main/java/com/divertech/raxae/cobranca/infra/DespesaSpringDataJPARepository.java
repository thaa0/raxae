package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.domain.StatusDespesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DespesaSpringDataJPARepository extends JpaRepository<Despesa, UUID> {

    @Query("SELECT d FROM Despesa d WHERE d.diaVencimento BETWEEN :diaInicio AND :diaFim AND d.status = :status")
    List<Despesa> findByDiaVencimentoBetweenAndStatus(@Param("diaInicio") Integer diaInicio,
                                                      @Param("diaFim") Integer diaFim,
                                                      @Param("status") StatusDespesa status);

List<Despesa> findByGrupoIdAndCriadoPorId(UUID grupoId, UUID criadoPorId);

    List<Despesa> findByGrupoId(UUID grupoId);
}