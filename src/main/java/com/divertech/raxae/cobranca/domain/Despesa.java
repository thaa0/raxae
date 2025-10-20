package com.divertech.raxae.cobranca.domain;

import com.divertech.raxae.cobranca.application.controller.DespesaRequest;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.divertech.raxae.cobranca.domain.TipoDivisao;
import com.divertech.raxae.cobranca.domain.TipoRecorrencia;
import com.divertech.raxae.cobranca.domain.StatusDespesa;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    @ManyToOne
    @JoinColumn(name = "criado_por_usuario_id", nullable = false)
    private Usuario criadoPor;

    @Column(nullable = false)
    private String nome;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    private Integer diaVencimento;

    private LocalDate dataVencimentoAvulsa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDivisao tipoDivisao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRecorrencia tipoRecorrencia;

    private String pixBeneficiado;

    @Column(nullable = false)
    private LocalDateTime momentoCriacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDespesa status;

    public Despesa(Grupo grupo, Usuario criadoPor, DespesaRequest request) {
        this.grupo = grupo;
        this.criadoPor = criadoPor;
        this.nome = request.getNome();
        this.valor = request.getValor();
        this.tipoDivisao = request.getTipoDivisao();
        this.tipoRecorrencia = request.getTipoRecorrencia();
        this.pixBeneficiado = request.getPixBeneficiado();
        this.momentoCriacao = LocalDateTime.now();
        this.status = StatusDespesa.ATIVA;
        if (this.tipoRecorrencia == TipoRecorrencia.UNICA) {
            this.dataVencimentoAvulsa = request.getDataVencimentoAvulsa();
        } else {
            this.diaVencimento = request.getDiaVencimento();
        }
    }

    public void setStatus(StatusDespesa status) {
        this.status = status;
    }
}