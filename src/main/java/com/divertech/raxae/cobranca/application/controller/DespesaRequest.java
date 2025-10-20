package com.divertech.raxae.cobranca.application.controller;

import com.divertech.raxae.cobranca.domain.TipoDivisao;
import com.divertech.raxae.cobranca.domain.TipoRecorrencia;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Getter
public class DespesaRequest {

    @NotBlank(message = "Nome (descrição) é obrigatório")
    private String nome;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "Tipo de recorrência é obrigatório")
    private TipoRecorrencia tipoRecorrencia;

    @NotNull(message = "Tipo de divisão é obrigatório")
    private TipoDivisao tipoDivisao;

    @FutureOrPresent(message = "Data de vencimento (avulsa) não pode ser no passado.")
    private LocalDate dataVencimentoAvulsa;

    @Min(value = 1, message = "O dia de vencimento deve ser entre 1 e 31.")
    @Max(value = 31, message = "O dia de vencimento deve ser entre 1 e 31.")
    private Integer diaVencimento;

    private Map<UUID, BigDecimal> divisoesEspecificas;

    private String pixBeneficiado;
    
}