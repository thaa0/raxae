package com.divertech.raxae.grupo.application.controller;

import com.divertech.raxae.grupo.domain.StatusParticipacao;
import lombok.Getter;

@Getter
public class MembroResponse {
    private String nomeCompleto;
    private StatusParticipacao status;
}
