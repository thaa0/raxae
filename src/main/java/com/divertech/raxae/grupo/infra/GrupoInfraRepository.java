package com.divertech.raxae.grupo.infra;

import com.divertech.raxae.grupo.application.controller.GrupoEditaRequest;
import com.divertech.raxae.grupo.application.repository.GrupoRepository;
import com.divertech.raxae.grupo.domain.Grupo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Log4j2
public class GrupoInfraRepository implements GrupoRepository {
    private final GrupoSpringDataJPARepository grupoSpringDataJPARepository;

    @Override
    public Optional<Grupo> buscaGrupoPorId(UUID idDoGrupo) {
        log.info("[start] GrupoInfraRepository - buscaGrupoPorId");
        Optional<Grupo> grupo = grupoSpringDataJPARepository.findById(idDoGrupo);
        log.debug("[finish] GrupoInfraRepository - buscaGrupoPorId");
        return grupo;
    }

    @Override
    public void apagaGrupo(Grupo grupo) {
        log.info("[start] GrupoInfraRepository - apagaGrupo");
        grupoSpringDataJPARepository.delete(grupo);
        log.debug("[finish] GrupoInfraRepository - apagaGrupo");
    }

    @Override
    public void salva(Grupo grupo) {
        log.info("[start] GrupoInfraRepository - salva");
        grupoSpringDataJPARepository.save(grupo);
        log.debug("[finish] GrupoInfraRepository - salva");
    }

    @Override
    public void editarGrupo(UUID idDoGrupo, GrupoEditaRequest grupoEditaRequest) {
        log.info("[start] GrupoInfraRepository - editarGrupo");
        grupoSpringDataJPARepository.findById(idDoGrupo).ifPresent(grupo -> {
            grupo.atualizaInformacoes(grupoEditaRequest);
            grupoSpringDataJPARepository.save(grupo);
        });
        log.debug("[finish] GrupoInfraRepository - editarGrupo");
    }
}
