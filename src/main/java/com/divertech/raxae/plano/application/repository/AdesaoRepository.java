package com.divertech.raxae.plano.application.repository;

import com.divertech.raxae.plano.domain.Adesao;
import java.util.UUID;

public interface AdesaoRepository {
    Adesao buscaPorUsuarioId(UUID usuarioId);
    Adesao salvar(Adesao adesao);
}