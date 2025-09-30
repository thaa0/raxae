package com.divertech.raxae.grupo.application.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AdicionarMembroRequest(
    @NotBlank(message = "O e-mail não pode ser em branco.")
    @Email(message = "O e-mail informado é inválido.")
    String email
) {}