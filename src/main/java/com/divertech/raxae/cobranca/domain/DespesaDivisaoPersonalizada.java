package com.divertech.raxae.cobranca.domain;

import com.divertech.raxae.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "despesa_divisao_personalizada")
public class DespesaDivisaoPersonalizada {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "despesa_id", nullable = false, insertable = false, updatable = false)
    private Despesa despesa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorPersonalizado;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentualPersonalizado;


    public DespesaDivisaoPersonalizada(Despesa despesa, Usuario usuario, BigDecimal valor, BigDecimal percentual, TipoDivisao tipoDivisao) {
        this.despesa = despesa;
        this.usuario = usuario;

        if (tipoDivisao == TipoDivisao.POR_VALOR) {
            this.valorPersonalizado = valor;
            this.percentualPersonalizado = null;
        } else if (tipoDivisao == TipoDivisao.POR_PERCENTUAL) {
            this.valorPersonalizado = null;
            this.percentualPersonalizado = percentual;
        }
    }
}