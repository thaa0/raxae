package com.divertech.raxae.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Token {
    private String tipo;
    private String token;
    private UUID usuarioId;

}
