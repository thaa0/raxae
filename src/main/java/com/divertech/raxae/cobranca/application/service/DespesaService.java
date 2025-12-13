package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.application.controller.DespesaRequest;
import com.divertech.raxae.cobranca.application.controller.DespesaResponse;

import java.util.List;
import java.util.UUID;
public interface DespesaService {

    DespesaResponse registraDespesa(UUID grupoId, DespesaRequest request, String emailUsuarioLogado);

    void excluiDespesa(UUID grupoId, UUID despesaId, String emailUsuarioLogado);
    
    List<DespesaResponse> listarDespesasDoGrupo(UUID grupoId, String emailUsuarioLogado);

}