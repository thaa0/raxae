package com.divertech.raxae.notificacao.application.service;

import com.divertech.raxae.notificacao.client.MaytapiWhatsAppClient;
import com.divertech.raxae.notificacao.client.dto.WhatsAppMessageRequest;
import com.divertech.raxae.notificacao.client.dto.WhatsAppMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Serviço para envio de mensagens via WhatsApp usando API Maytapi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WhatsAppService {

    private final MaytapiWhatsAppClient maytapiClient;

    @Value("${maytapi.product-id}")
    private String productId;

    @Value("${maytapi.phone-id}")
    private String phoneId;

    @Value("${maytapi.api-key}")
    private String apiKey;

    /**
     * Envia mensagem de texto via WhatsApp
     *
     * @param numeroWhatsApp Número do destinatário (com código do país, sem caracteres especiais)
     * @param mensagem Conteúdo da mensagem
     * @return true se enviado com sucesso, false caso contrário
     */
    public boolean enviarMensagem(String numeroWhatsApp, String mensagem) {
        try {
            log.info("Enviando mensagem WhatsApp para: {}", numeroWhatsApp);

            WhatsAppMessageRequest request = WhatsAppMessageRequest.builder()
                    .toNumber(numeroWhatsApp)
                    .type("text")
                    .message(mensagem)
                    .typing("typing")
                    .duration(3)
                    .build();

            WhatsAppMessageResponse response = maytapiClient.sendMessage(
                    productId,
                    phoneId,
                    apiKey,
                    request
            );

            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
                log.info("Mensagem enviada com sucesso para: {}", numeroWhatsApp);
                return true;
            } else {
                log.warn("Falha ao enviar mensagem para {}: {}",
                        numeroWhatsApp,
                        response != null ? response.getMessage() : "Resposta nula");
                return false;
            }

        } catch (Exception e) {
            log.error("Erro ao enviar mensagem WhatsApp para {}: {}",
                     numeroWhatsApp, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Envia mensagem com mídia via WhatsApp
     *
     * @param numeroWhatsApp Número do destinatário
     * @param urlMidia URL da mídia (imagem, vídeo, documento)
     * @param texto Texto opcional junto com a mídia
     * @return true se enviado com sucesso
     */
    public boolean enviarMensagemComMidia(String numeroWhatsApp, String urlMidia, String texto) {
        try {
            log.info("Enviando mensagem com mídia para: {}", numeroWhatsApp);

            WhatsAppMessageRequest request = WhatsAppMessageRequest.builder()
                    .toNumber(numeroWhatsApp)
                    .type("media")
                    .message(urlMidia)
                    .text(texto)
                    .build();

            WhatsAppMessageResponse response = maytapiClient.sendMessage(
                    productId,
                    phoneId,
                    apiKey,
                    request
            );

            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
                log.info("Mensagem com mídia enviada com sucesso para: {}", numeroWhatsApp);
                return true;
            } else {
                log.warn("Falha ao enviar mensagem com mídia para {}: {}",
                        numeroWhatsApp,
                        response != null ? response.getMessage() : "Resposta nula");
                return false;
            }

        } catch (Exception e) {
            log.error("Erro ao enviar mensagem com mídia para {}: {}",
                     numeroWhatsApp, e.getMessage(), e);
            return false;
        }
    }
}

