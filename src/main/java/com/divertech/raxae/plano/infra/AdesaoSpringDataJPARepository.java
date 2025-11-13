package com.divertech.raxae.plano.infra;

import com.divertech.raxae.plano.domain.Adesao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AdesaoSpringDataJPARepository extends JpaRepository<Adesao, UUID> {
    
    Optional<Adesao> findByIdUsuario(UUID usuarioId);
}