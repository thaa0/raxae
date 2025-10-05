package com.divertech.raxae.usuario.application.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String senha;

}
