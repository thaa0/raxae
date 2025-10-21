package com.divertech.raxae.grupo.infra;

import com.divertech.raxae.grupo.application.repository.MembroRepository;
import com.divertech.raxae.grupo.domain.Membro;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

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
}
