package com.divertech.raxae.plano.application.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.util.UUID;

@Getter
public class AtualizarPlanoRequest {

    @NotNull(message = "O ID do plano não pode ser nulo.")
    private UUID planoId;
}