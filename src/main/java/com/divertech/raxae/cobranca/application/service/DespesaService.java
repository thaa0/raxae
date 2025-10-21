package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.application.controller.DespesaRequest;
import com.divertech.raxae.cobranca.application.controller.DespesaResponse;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.usuario.domain.Usuario;
import com.divertech.raxae.cobranca.domain.Despesa;
import java.util.UUID;
public interface DespesaService {

    DespesaResponse registraDespesa(UUID grupoId, DespesaRequest request, String emailUsuarioLogado);

    void excluiDespesa(UUID grupoId, UUID despesaId, String emailUsuarioLogado);
    
}