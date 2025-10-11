package com.divertech.raxae.grupo.application.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AdicionarMembroRequest(
    @NotBlank(message = "O e-mail não pode ser nulo ou vazio.")
    @Email(message = "O formato do e-mail é inválido.")
    String email
) {}