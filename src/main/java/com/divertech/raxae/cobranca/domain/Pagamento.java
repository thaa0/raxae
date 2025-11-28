package com.divertech.raxae.cobranca.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "pagamento")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;
    
    @OneToOne()
    @JoinColumn(name = "cobranca_id", nullable = false)
    private Cobranca cobranca;

    
    @Column(name = "valor_pago", precision = 10, scale = 2)
    private BigDecimal valorPago;
    
    @Lob
    @Column(name = "comprovante", columnDefinition = "BLOB")
    private byte[] comprovante;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusPagamento status;
    
    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;
    
    @Column(name = "data_validacao")
    private LocalDateTime dataValidacao;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static Pagamento criar(Cobranca cobranca, BigDecimal valorPago, byte[] comprovante) {
        LocalDateTime agora = LocalDateTime.now();
        Pagamento pagamento = new Pagamento();
        pagamento.cobranca = cobranca;
        pagamento.valorPago = valorPago;
        pagamento.comprovante = comprovante;
        pagamento.status = StatusPagamento.ENVIADO;
        pagamento.dataEnvio = agora;
        pagamento.createdAt = agora;
        return pagamento;
    }
}