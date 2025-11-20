package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.domain.StatusDespesa;
import com.divertech.raxae.cobranca.repository.DespesaRepository;
import com.divertech.raxae.handler.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DespesaInfraRepository implements DespesaRepository {

    private final DespesaSpringDataJPARepository jpaRepository;

    @Override
    public Despesa salvar(Despesa despesa) {
        return jpaRepository.save(despesa);
    }

    @Override
    public Despesa buscaPorId(UUID id) {
        return jpaRepository.findById(id)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Despesa não encontrada!"));
    }

    @Override
    public List<Despesa> buscarPorDiaVencimentoEStatus(Integer diaVencimento, StatusDespesa status) {
        // Busca despesas desde hoje até o dia alvo
        Integer diaHoje = LocalDate.now().getDayOfMonth();
        return jpaRepository.findByDiaVencimentoBetweenAndStatus(diaHoje, diaVencimento, status);
    }
}