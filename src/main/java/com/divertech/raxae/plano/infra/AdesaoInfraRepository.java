package com.divertech.raxae.plano.infra;

import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.plano.application.repository.AdesaoRepository;
import com.divertech.raxae.plano.domain.Adesao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AdesaoInfraRepository implements AdesaoRepository {

    private final AdesaoSpringDataJPARepository jpaRepository;

    @Override
    public Adesao buscaPorUsuarioId(UUID usuarioId) {
        return jpaRepository.findByIdUsuario(usuarioId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Adesão de usuário não encontrada!"));
    }
    
    @Override
    public Adesao salvar(Adesao adesao) {
        return jpaRepository.save(adesao);
    }
}