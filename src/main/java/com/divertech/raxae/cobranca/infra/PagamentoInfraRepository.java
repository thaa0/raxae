package com.divertech.raxae.cobranca.infra;

import com.divertech.raxae.cobranca.domain.Pagamento;
import com.divertech.raxae.cobranca.repository.PagamentoRepository;
import com.divertech.raxae.handler.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PagamentoInfraRepository implements PagamentoRepository {

    private final PagamentoSpringDataJPARepository jpaRepository;

    @Override
    public Pagamento salvar(Pagamento pagamento) {
        return jpaRepository.save(pagamento);
    }

    @Override
    public Pagamento buscarPorCobrancaId(UUID cobrancaId) {
        return jpaRepository.findByCobrancaId(cobrancaId)
                .orElse(null);
    }


    @Override
    public Pagamento buscarPorId(UUID pagamentoId) {
        return jpaRepository.findById(pagamentoId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Pagamento não encontrado para estta cobrança."));
    }
}

