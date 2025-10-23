package com.divertech.raxae.grupo.infra;

import com.divertech.raxae.grupo.application.repository.MembroRepository;
import com.divertech.raxae.grupo.domain.Membro;
import com.divertech.raxae.handler.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Log4j2
public class MembroInfraRepository implements MembroRepository {
    private final MembroSpringDataJPARepository membroSpringDataJPARepository;

    @Override
    public Membro salva(Membro membro) {
        log.info("[start] MembroInfraRepository - salva");
        membroSpringDataJPARepository.save(membro);
        log.debug("[finish] MembroInfraRepository - salva");
        return membro;
    }

    @Override
    public Membro buscaMembro(UUID idDoMembro) {
        log.info("[start] MembroInfraRepository - buscaMembro");
        Membro membro =  membroSpringDataJPARepository.findById(idDoMembro)
                .orElseThrow(()-> APIException.build(HttpStatus.NOT_FOUND, "Membro não encontrado!"));
        log.debug("[finish] MembroInfraRepository - buscaMembro");
        return membro;
    }
}
