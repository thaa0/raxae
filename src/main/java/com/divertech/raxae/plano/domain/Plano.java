package com.divertech.raxae.plano.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Plano {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoPlano tipo;
    @NotBlank
    private String Descricao;
    private BigDecimal precoMensal;
    private int limiteGrupos;
    private int limiteMembrosPorGrupo;
    private int limiteDespesaPorGrupo;
}
