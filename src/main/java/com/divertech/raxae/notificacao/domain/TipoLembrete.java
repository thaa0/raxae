package com.divertech.raxae.notificacao.domain;

public enum TipoLembrete {
    VENCE_DAQUI_2_DIAS("Vence Daqui 2 Dias"),
    VENCE_AMANHA("Vence Amanhã"),
    VENCE_HOJE("Vence Hoje");

    private final String descricao;

    TipoLembrete(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
