package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PagamentoSpringDataJPARepository extends JpaRepository<Pagamento, UUID> {
    Optional<Pagamento> findByCobrancaId(UUID cobrancaId);
}

