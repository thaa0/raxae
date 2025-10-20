package com.divertech.raxae.grupo.application.controller;

import com.divertech.raxae.grupo.domain.StatusParticipacao;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MembroResponse {
    private String nomeCompleto;
    private StatusParticipacao status;

    public MembroResponse(@NotBlank(message = "Campo não pode ser em branco") String nomeCompleto, StatusParticipacao status) {
        this.nomeCompleto = nomeCompleto;
        this.status = status;
    }
}
