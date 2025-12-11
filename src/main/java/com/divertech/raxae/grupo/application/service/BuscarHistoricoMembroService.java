package com.divertech.raxae.grupo.application.service;

import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
import com.divertech.raxae.cobranca.repository.DespesaRepository;
import com.divertech.raxae.grupo.application.repository.MembroRepository;
import com.divertech.raxae.grupo.domain.Membro;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuscarHistoricoMembroService {

    private final CobrancaRepository cobrancaRepository;
    private final DespesaRepository despesaRepository;
    private final MembroRepository membroRepository;


    @Transactional(readOnly = true)
    public HistoricoMembroResponse executar(UUID groupId, UUID memberId) {
        log.info("[BuscarHistoricoMembro] Iniciando busca. Grupo: {}, Membro: {}", groupId, memberId);
        Membro membro = membroRepository.buscaMembro(memberId);
        Usuario usuario = membro.getUsuario();
        List<Despesa> despesas = despesaRepository.findByGrupoId(groupId);
        List<Cobranca> cobrancas = cobrancaRepository.findByGrupoIdAndUsuarioId(groupId, usuario.getId());

        List<ItemHistoricoResponse> itens = new ArrayList<>();

        despesas.forEach(d -> itens.add(ItemHistoricoResponse.builder()
                .id(d.getId())
                .tipo("DESPESA")
                .descricao(d.getNome())
                .valor(d.getValor())
                .data(d.getMomentoCriacao())
                .status(d.getStatus().name())
                .build()));

        cobrancas.forEach(c -> {
            LocalDateTime dataRef = c.getDataPagamento() != null 
                ? c.getDataPagamento().atStartOfDay() 
                : c.getMomentoCriacao();

            itens.add(ItemHistoricoResponse.builder()
                .id(c.getId())
                .tipo("COBRANCA")
                .descricao("Parc: " + c.getDespesa().getNome())
                .valor(c.getValor())
                .data(dataRef)
                .status(c.getStatus().name())
                .build());
        });

        itens.sort(Comparator.comparing(ItemHistoricoResponse::getData).reversed());

        BigDecimal totalDespesas = despesas.stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalCobrancas = cobrancas.stream()
                .map(Cobranca::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return HistoricoMembroResponse.builder()
                .grupoId(groupId)
                .membroId(memberId)
                .totalDespesasRealizadas(totalDespesas)
                .totalCobrancasRecebidas(totalCobrancas)
                .saldo(totalDespesas.subtract(totalCobrancas))
                .historico(itens)
                .build();
    }
}