package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.domain.StatusCobranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

public interface CobrancaSpringDataJPARepository extends JpaRepository<Cobranca, UUID> {
       List<Cobranca> findByDespesaId(UUID despesaId);

       @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cobranca c " +
                     "WHERE c.despesa.id = :despesaId AND c.mesReferencia = :mesReferencia")
       boolean existsByDespesaIdAndMesReferencia(@Param("despesaId") UUID despesaId,
                     @Param("mesReferencia") String mesReferencia);

       @Query("SELECT c FROM Cobranca c " +
                     "WHERE c.status = :status " +
                     "AND c.mesReferencia = :mesReferencia " +
                     "AND c.dataVencimento = :dataVencimento")
       List<Cobranca> findByStatusAndMesReferenciaAndDataVencimento(
                     @Param("status") StatusCobranca status,
                     @Param("mesReferencia") String mesReferencia,
                     @Param("dataVencimento") LocalDate dataVencimento);

       List<Cobranca> findByDespesaGrupoIdAndUsuarioId(UUID grupoId, UUID usuarioId);

       @Query("SELECT c FROM Cobranca c " +
                     "WHERE c.despesa.id = :despesaId AND c.usuario.id = :usuarioId")
       Cobranca findByDespesaIdAndUsuarioId(
                     @Param("despesaId") UUID despesaId,
                     @Param("usuarioId") UUID usuarioId);

       @Query("SELECT c FROM Cobranca c " +
                     "JOIN FETCH c.despesa " +
                     "WHERE c.usuario.id = :usuarioId " +
                     "AND c.status = :status " +
                     "AND c.dataPagamento IS NOT NULL " +
                     "AND YEAR(c.dataPagamento) = :ano " +
                     "AND MONTH(c.dataPagamento) = :mes")
       List<Cobranca> findByUsuarioIdAndStatusAndDataPagamentoNoMes(
                     @Param("usuarioId") UUID usuarioId,
                     @Param("status") StatusCobranca status,
                     @Param("ano") int ano,
                     @Param("mes") int mes);

       @Query("SELECT c FROM Cobranca c " +
                     "JOIN FETCH c.despesa " +
                     "WHERE c.usuario.id = :usuarioId " +
                     "AND c.status = :status")
       List<Cobranca> findByUsuarioIdAndStatus(
                     @Param("usuarioId") UUID usuarioId,
                     @Param("status") StatusCobranca status);

       @Query("SELECT c FROM Cobranca c " +
                     "WHERE c.despesa.criadoPor.id = :usuarioId " +
                     "AND c.status = :status")
       List<Cobranca> findByDespesaCriadoPorIdAndStatus(
                     @Param("usuarioId") UUID usuarioId,
                     @Param("status") StatusCobranca status);

       @Query("SELECT c FROM Cobranca c " +
                     "JOIN FETCH c.despesa d " +
                     "JOIN FETCH d.grupo " +
                     "WHERE c.usuario.id = :usuarioId " +
                     "ORDER BY c.dataVencimento DESC")
       List<Cobranca> findByUsuarioId(@Param("usuarioId") UUID usuarioId);

       @Query("SELECT c FROM Cobranca c " +
                     "JOIN FETCH c.despesa d " +
                     "JOIN FETCH d.grupo g " +
                     "WHERE g.id = :grupoId " +
                     "ORDER BY c.dataVencimento DESC")
       List<Cobranca> findByDespesaGrupoId(@Param("grupoId") UUID grupoId);
}