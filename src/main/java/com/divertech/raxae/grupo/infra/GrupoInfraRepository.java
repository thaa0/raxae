package com.divertech.raxae.grupo.infra;

import com.divertech.raxae.grupo.application.repository.GrupoRepository;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.handler.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Log4j2
public class GrupoInfraRepository implements GrupoRepository {
    private final GrupoSpringDataJPARepository grupoSpringDataJPARepository;
    @Override
    public Grupo buscaGrupoPorId(UUID idDoGrupo) {
        log.info("[start] GrupoInfraRepository - buscaGrupoPorId");
        Grupo grupo = grupoSpringDataJPARepository.findById(idDoGrupo)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Grupo não encontrado!"));
        log.debug("[finish] GrupoInfraRepository - buscaGrupoPorId");
        return grupo;

    }

    @Override
    public void apagaGrupo(Grupo grupo) {
        log.info("[start] GrupoInfraRepository - apagaGrupoPorId");
        grupoSpringDataJPARepository.delete(grupo);
        log.debug("[finish] GrupoInfraRepository - apagaGrupoPorId");
    }

    @Override
    public void salva(Grupo grupo) {
        log.info("[start] GrupoInfraRepository - salva");
        grupoSpringDataJPARepository.save(grupo);
        log.debug("[finish] GrupoInfraRepository - salva");
    }
}
