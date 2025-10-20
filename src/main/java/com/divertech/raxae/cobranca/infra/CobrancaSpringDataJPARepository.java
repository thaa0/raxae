package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Cobranca;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CobrancaSpringDataJPARepository extends JpaRepository<Cobranca, UUID> {
}