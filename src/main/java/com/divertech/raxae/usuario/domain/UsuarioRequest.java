package com.divertech.raxae.usuario.domain;

import lombok.Getter;

@Getter
public class UsuarioRequest {
    private String nomeCompleto;
    private String whatsapp;
    private String email;
    private String senha;
}
