package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.application.controller.CobrancaResponse;

import java.util.List;
import java.util.UUID;

public interface CobrancaService {
    List<CobrancaResponse> listarCobrancasPorUsuario(UUID usuarioId);

    List<CobrancaResponse> listarCobrancasPorGrupo(UUID grupoId, UUID usuarioAdminId);
}
