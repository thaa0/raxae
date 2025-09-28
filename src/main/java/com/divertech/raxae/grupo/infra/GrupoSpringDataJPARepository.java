package com.divertech.raxae.grupo.infra;

import com.divertech.raxae.grupo.domain.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


public interface GrupoSpringDataJPARepository extends JpaRepository<Grupo, UUID> {
}