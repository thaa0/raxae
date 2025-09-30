package com.divertech.raxae.grupo.application.repository;

import com.divertech.raxae.grupo.domain.Membro;
import java.util.UUID;

public interface MembroRepository {
    Membro salva(Membro membro);
    boolean existeMembroNoGrupo(UUID idGrupo, UUID idUsuario);
}