package com.divertech.raxae.cobranca.domain;


import com.divertech.raxae.usuario.domain.Usuario;
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
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;
    @OneToOne()
    @JoinColumn(name = "cobranca_id", nullable = false)
    private Cobranca cobranca;
    private String comprovante;
    private StatusPagamento statusPagamento;
    private LocalDateTime momentoEnvio;
    private LocalDate dataConfirmacao;
    private LocalDateTime momentoCriacao;
}