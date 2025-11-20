package com.divertech.raxae.cobranca.domain;

import com.divertech.raxae.cobranca.application.controller.DespesaRequest;
import com.divertech.raxae.cobranca.application.controller.DivisaoRequest;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.domain.Usuario;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @ElementCollection
    @CollectionTable(name = "despesa_divisoes_especificas", joinColumns = @JoinColumn(name = "despesa_id"))
    @MapKeyColumn(name = "usuario_id")
    @Column(name = "valor", precision = 10, scale = 2)
    private Map<UUID, BigDecimal> divisoesEspecificas;

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
        this.diaVencimento = request.getDiaVencimento();
        this.divisoesEspecificas = request.getDivisoesEspecificas();
    }

    public void setStatus(StatusDespesa status) {
        if (this.status == status) {
            String msgErro = String.format("Esta despesa já está %s", status);
            throw APIException.build(HttpStatus.CONFLICT, msgErro);
        }
        this.status = status;
    }
}