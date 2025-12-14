package com.divertech.raxae.notificacao.application.service;

import com.divertech.raxae.notificacao.domain.TipoLembrete;
import org.springframework.stereotype.Component;

@Component
public class MensagemLembreteBuilder {

    public String construirMensagem(TipoLembrete tipoLembrete, String nomeDespesa, String nomeUsuario, String pixBeneficiario) {
        return switch (tipoLembrete) {
            case VENCE_DAQUI_2_DIAS -> construirMensagemVenceDaqui2Dias(nomeDespesa, nomeUsuario, pixBeneficiario);
            case VENCE_AMANHA -> construirMensagemVenceAmanha(nomeDespesa, nomeUsuario, pixBeneficiario);
            case VENCE_HOJE -> construirMensagemVenceHoje(nomeDespesa, nomeUsuario, pixBeneficiario);
        };
    }

    private String construirMensagemVenceDaqui2Dias(String nomeDespesa, String nomeUsuario, String pixBeneficiario) {
        return String.format(
                "🔔 Atenção %s!\n\n" +
                "A conta de *%s* vence daqui a 2 dias! 📅\n\n" +
                "Não deixa acumular, hein! Bora quitar logo! 💰\n\n" +
                "💳 *PIX:* %s",
                nomeUsuario,
                nomeDespesa,
                pixBeneficiario
        );
    }

    private String construirMensagemVenceAmanha(String nomeDespesa, String nomeUsuario, String pixBeneficiario) {
        return String.format(
                "⏰ Ei %s, corre aqui!\n\n" +
                "A conta de *%s* vence AMANHÃ! 📆\n\n" +
                "Última chance de quitar sem atraso! 💸\n\n" +
                "💳 *PIX:* %s",
                nomeUsuario,
                nomeDespesa,
                pixBeneficiario
        );
    }

    private String construirMensagemVenceHoje(String nomeDespesa, String nomeUsuario, String pixBeneficiario) {
        return String.format(
                "🔔 Ei %s, olha a treta chegando!\n\n" +
                "A conta de *%s* vence HOJE! 📅\n\n" +
                "Bora quitar antes que vire novela... 💸\n\n" +
                "💳 *PIX:* %s",
                nomeUsuario,
                nomeDespesa,
                pixBeneficiario
        );
    }
}

