package com.divertech.raxae.usuario.application.controller;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class InfoUsuarioResponse {
    int numeroDeGrupo;
    double economiaTotal;
    double totalPagoNoMes;

    public InfoUsuarioResponse(@NotNull(message = "O campo não pode ser em branco") int numeroDeGrupo, double economiaTotal, double totalPagoNoMes) {
        this.numeroDeGrupo = numeroDeGrupo;
        this.economiaTotal = economiaTotal;
        this.totalPagoNoMes = totalPagoNoMes;
    }

}
