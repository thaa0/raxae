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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "despesa_id", referencedColumnName = "id", nullable = false)
    private List<DespesaDivisaoPersonalizada> divisoesPersonalizadas = new ArrayList<>();

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
    }

    public void setStatus(StatusDespesa status) {
        if (this.status == status) {
            String msgErro = String.format("Esta despesa já está %s", status);
            throw APIException.build(HttpStatus.CONFLICT, msgErro);
        }
        this.status = status;
    }

    public void processarDivisoes(List<DivisaoRequest> divisoesRequest, UsuarioRepository usuarioRepository) {
        this.divisoesPersonalizadas.clear();

        if (this.tipoDivisao == TipoDivisao.IGUALITARIA) {
            return;
        }

        if (divisoesRequest == null || divisoesRequest.isEmpty()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Lista de divisões vazia");
        }

        BigDecimal somaValores = BigDecimal.ZERO;
        BigDecimal somaPercentuais = BigDecimal.ZERO;

        for (DivisaoRequest req : divisoesRequest) {
            if (req == null || req.getUsuarioId() == null) {
                throw APIException.build(HttpStatus.BAD_REQUEST, "Divisão inválida: usuário obrigatório");
            }

            Usuario membro = usuarioRepository.buscaUsuarioPorId(req.getUsuarioId());

            BigDecimal valorReq = req.getValor();
            BigDecimal percentualReq = req.getPercentual();

            if (this.tipoDivisao == TipoDivisao.POR_VALOR) {
                if (valorReq == null || valorReq.compareTo(BigDecimal.ZERO) <= 0) {
                    throw APIException.build(HttpStatus.BAD_REQUEST, "Valor obrigatório e maior que zero para divisão por valor");
                }
                somaValores = somaValores.add(valorReq);
            } else if (this.tipoDivisao == TipoDivisao.POR_PERCENTUAL) {
                if (percentualReq == null || percentualReq.compareTo(BigDecimal.ZERO) <= 0) {
                    throw APIException.build(HttpStatus.BAD_REQUEST, "Percentual obrigatório e maior que zero para divisão por percentual");
                }
                somaPercentuais = somaPercentuais.add(percentualReq);
            } else {
                throw APIException.build(HttpStatus.BAD_REQUEST, "Tipo de divisão inválido");
            }

            DespesaDivisaoPersonalizada divisao = new DespesaDivisaoPersonalizada(
                    this,
                    membro,
                    valorReq,
                    percentualReq,
                    this.tipoDivisao
            );

            this.divisoesPersonalizadas.add(divisao);
        }

        if (this.tipoDivisao == TipoDivisao.POR_VALOR) {
            if (somaValores.compareTo(this.valor) != 0) {
                throw APIException.build(HttpStatus.BAD_REQUEST, "A soma dos valores personalizados deve ser igual ao valor da despesa");
            }
        } else if (this.tipoDivisao == TipoDivisao.POR_PERCENTUAL) {
            if (somaPercentuais.compareTo(new BigDecimal("100")) != 0) {
                throw APIException.build(HttpStatus.BAD_REQUEST, "A soma dos percentuais personalizados deve ser igual a 100");
            }
        }
    }
}