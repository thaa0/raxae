package com.divertech.raxae.usuario.application.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String senha;

}
