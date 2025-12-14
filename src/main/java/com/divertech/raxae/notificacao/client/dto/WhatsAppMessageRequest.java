package com.divertech.raxae.notificacao.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppMessageRequest {

    @JsonProperty("to_number")
    private String toNumber;

    @JsonProperty("type")
    private String type; // "text", "media", etc.

    @JsonProperty("message")
    private String message;

    @JsonProperty("typing")
    private String typing; // "typing" or "recording" (opcional)

    @JsonProperty("duration")
    private Integer duration; // 1-30 (opcional)
}

