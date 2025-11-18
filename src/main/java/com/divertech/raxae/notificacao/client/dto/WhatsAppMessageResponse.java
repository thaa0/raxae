package com.divertech.raxae.notificacao.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta da API Maytapi WhatsApp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppMessageResponse {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Object data;
}

