package com.divertech.raxae.notificacao.application.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PayloadWpp {
    private String to_number;
    private String type;
    private String message;
}
