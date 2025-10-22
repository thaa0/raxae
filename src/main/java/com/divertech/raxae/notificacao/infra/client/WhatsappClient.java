package com.divertech.raxae.notificacao.infra.client;

import com.divertech.raxae.notificacao.application.controller.PayloadWpp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "WhatsappClient", url = "https://api.maytapi.com/api/")
public interface WhatsappClient {
    @PostMapping("{productId}/{phoneId}/sendMessage")
    void sendMessage(@PathVariable String productId,
                     @PathVariable String phoneId,
                     @RequestBody PayloadWpp payload);

}
