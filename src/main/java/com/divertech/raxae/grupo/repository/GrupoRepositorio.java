package com.divertech.raxae.grupo.repository;

import com.divertech.raxae.grupo.domain.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GrupoRepositorio extends JpaRepository<Grupo, UUID> {
}