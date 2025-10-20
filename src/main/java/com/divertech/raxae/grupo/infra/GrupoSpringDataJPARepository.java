package com.divertech.raxae.grupo.infra;

import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.grupo.domain.StatusParticipacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface GrupoSpringDataJPARepository extends JpaRepository<Grupo, UUID> {
    List<Grupo> findByMembrosUsuarioIdAndMembrosStatus(UUID usuarioId, StatusParticipacao status);
}