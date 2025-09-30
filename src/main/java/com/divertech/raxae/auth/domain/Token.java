package com.divertech.raxae.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Token {
    private String tipo;
    private String token;
    private UUID usuarioId;

}
