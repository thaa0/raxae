package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.domain.StatusDespesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DespesaSpringDataJPARepository extends JpaRepository<Despesa, UUID> {

    @Query("SELECT d FROM Despesa d WHERE d.diaVencimento = :diaVencimento AND d.status = :status")
    List<Despesa> findByDiaVencimentoAndStatus(@Param("diaVencimento") Integer diaVencimento,
                                                 @Param("status") StatusDespesa status);
}