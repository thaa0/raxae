package com.divertech.raxae.notificacao.client;

import com.divertech.raxae.notificacao.client.dto.WhatsAppMessageRequest;
import com.divertech.raxae.notificacao.client.dto.WhatsAppMessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign Client para integração com API Maytapi WhatsApp
 */
@FeignClient(
    name = "maytapi-whatsapp",
    url = "${maytapi.base-url}",
    configuration = MaytapiClientConfig.class
)
public interface MaytapiWhatsAppClient {

    @PostMapping("/api/{productId}/{phoneId}/sendMessage")
    WhatsAppMessageResponse sendMessage(
        @PathVariable("productId") String productId,
        @PathVariable("phoneId") String phoneId,
        @RequestHeader("x-maytapi-key") String apiKey,
        @RequestBody WhatsAppMessageRequest request
    );
}

