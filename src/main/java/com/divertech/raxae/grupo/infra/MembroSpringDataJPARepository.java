package com.divertech.raxae.grupo.infra;

import com.divertech.raxae.grupo.domain.Membro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MembroSpringDataJPARepository extends JpaRepository<Membro,UUID> {
}
