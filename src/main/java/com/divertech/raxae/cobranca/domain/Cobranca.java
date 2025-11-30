package com.divertech.raxae.cobranca.domain;

import com.divertech.raxae.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Cobranca {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "despesa_id", nullable = false)
    private Despesa despesa;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @Column(precision = 10, scale = 2)
    private BigDecimal valor;
    @Setter
    @Enumerated(EnumType.STRING)
    private StatusCobranca status;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private LocalDateTime momentoCriacao;
    @Column(nullable = false, length = 7)
    private String mesReferencia; // Formato: YYYY-MM (ex: "2025-11")

    
    public Cobranca(Despesa despesa, Usuario usuario, BigDecimal valor, StatusCobranca status, LocalDate dataVencimento) {
        this.despesa = despesa;
        this.usuario = usuario;
        this.valor = valor;
        this.status = status;
        this.dataVencimento = dataVencimento;
        this.momentoCriacao = LocalDateTime.now();
        this.mesReferencia = YearMonth.from(dataVencimento).toString();
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

}