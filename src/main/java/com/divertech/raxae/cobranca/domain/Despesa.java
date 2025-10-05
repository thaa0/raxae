package com.divertech.raxae.cobranca.domain;

import com.divertech.raxae.grupo.domain.Grupo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;
    private String nome;
    @Column(precision = 10, scale = 2)
    private BigDecimal valor;
    private Integer diaVencimento;
    private TipoDivisao tipoDivisao;
    private String pixBeneficiado;
    private LocalDateTime momentoCriacao;
}
