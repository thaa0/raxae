package com.divertech.raxae.grupo.infra;

import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.grupo.domain.StatusParticipacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import com.divertech.raxae.grupo.domain.StatusGrupo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GrupoSpringDataJPARepository extends JpaRepository<Grupo, UUID> {
    @Query("SELECT g FROM Grupo g JOIN g.membros m " +
            "WHERE m.usuario.id = :usuarioId " +
            "AND m.status = :statusMembro " +
            "AND (g.status = :statusGrupo OR g.status IS NULL)")
    List<Grupo> findGruposAtivosPorUsuario(@Param("usuarioId") UUID usuarioId,
            @Param("statusMembro") StatusParticipacao statusMembro,
            @Param("statusGrupo") StatusGrupo statusGrupo);
}