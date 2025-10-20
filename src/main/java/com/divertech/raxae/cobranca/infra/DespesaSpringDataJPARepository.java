package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DespesaSpringDataJPARepository extends JpaRepository<Despesa, UUID> {
}