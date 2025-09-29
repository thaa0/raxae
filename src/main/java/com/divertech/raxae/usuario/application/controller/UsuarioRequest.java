package com.divertech.raxae.usuario.application.controller;

import lombok.Getter;

@Getter
public class UsuarioRequest {
    private String nomeCompleto;
    private String whatsapp;
    private String email;
    private String senha;
}
