package com.divertech.raxae.notificacao.client;

import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Feign Client para Maytapi WhatsApp
 */
@Configuration
@Slf4j
public class MaytapiClientConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new MaytapiErrorDecoder();
    }

    /**
     * Decodificador customizado de erros da API Maytapi
     */
    static class MaytapiErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, feign.Response response) {
            log.error("Erro ao chamar API Maytapi - Status: {} - Método: {}",
                     response.status(), methodKey);
            return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}
