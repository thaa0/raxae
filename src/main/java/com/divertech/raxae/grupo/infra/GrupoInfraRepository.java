package com.divertech.raxae.grupo.infra;

import com.divertech.raxae.grupo.application.controller.GrupoEditaRequest;
import com.divertech.raxae.grupo.application.repository.GrupoRepository;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.grupo.domain.StatusParticipacao;
import com.divertech.raxae.handler.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    public void editarGrupo(UUID idDoGrupo, GrupoEditaRequest grupoEditaRequest) {
        log.info("[start] GrupoInfraRepository - editarGrupo");
        Optional<Grupo> grupoOptional = grupoSpringDataJPARepository.findById(idDoGrupo);
        if (grupoOptional.isEmpty()) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Grupo não encontrado!");
        }
        Grupo grupo = grupoOptional.get();
        grupo.atualizaInformacoes(grupoEditaRequest);
        grupoSpringDataJPARepository.save(grupo);
        log.debug("[finish] GrupoInfraRepository - editarGrupo");
    }

    @Override
    public List<Grupo> buscaGruposPorUsuario(UUID id) {
        log.info("[start] GrupoInfraRepository - buscaGruposPorUsuario");
        List<Grupo> grupos = grupoSpringDataJPARepository.findByMembrosUsuarioIdAndMembrosStatus(id, StatusParticipacao.ATIVO);
        log.debug("[finish] GrupoInfraRepository - buscaGruposPorUsuario");
        return grupos;
    }
}