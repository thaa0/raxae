package com.divertech.raxae.plano.application.controller;

import com.divertech.raxae.plano.domain.Adesao;
import com.divertech.raxae.plano.domain.StatusAdesao;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class AdesaoResponse {

    private UUID id;
    private StatusAdesao statusAdesao;
    private LocalDateTime momentoAdesao;
    private int diaExpiracao;
    private UUID usuarioId;
    private UUID planoId;

    public AdesaoResponse(Adesao adesao) {
        this.id = adesao.getId();
        this.statusAdesao = adesao.getStatusAdesao();
        this.momentoAdesao = adesao.getMomentoAdesao();
        this.diaExpiracao = adesao.getDiaExpiracao();
        this.usuarioId = adesao.getIdUsuario();
        this.planoId = adesao.getPlano().getId();
    }
}