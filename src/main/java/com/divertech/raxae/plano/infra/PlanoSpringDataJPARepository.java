package com.divertech.raxae.plano.infra;

import com.divertech.raxae.plano.domain.Plano;
import com.divertech.raxae.plano.domain.TipoPlano;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; 
import java.util.UUID;

public interface PlanoSpringDataJPARepository extends JpaRepository<Plano, UUID> {
    
    Optional<Plano> findByTipo(TipoPlano tipo);
}