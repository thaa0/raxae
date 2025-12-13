package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.application.controller.DespesaRequest;
import com.divertech.raxae.cobranca.application.controller.DespesaResponse;
import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.domain.StatusCobranca;
import com.divertech.raxae.cobranca.domain.StatusDespesa;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
import com.divertech.raxae.cobranca.repository.DespesaRepository;
import com.divertech.raxae.grupo.application.repository.GrupoRepository;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class DespesaApplicationService implements DespesaService {

    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;
    private final DespesaRepository despesaRepository;
    private final CobrancaRepository cobrancaRepository;

    @Override
    @Transactional
    public DespesaResponse registraDespesa(UUID grupoId, DespesaRequest request, String emailUsuarioLogado) {
        log.info("[start] DespesaApplicationService - registraDespesa");
        Usuario admin = usuarioRepository.buscaUsuarioPorEmail(emailUsuarioLogado);
        Grupo grupo = grupoRepository.buscaGrupoPorId(grupoId);
        
        if (grupo.buscaMembro(admin) == null) {
             throw APIException.build(HttpStatus.FORBIDDEN, "Usuário não pertence ao grupo.");
        }

        Despesa despesa = despesaRepository.salvar(new Despesa(grupo, admin, request));
        
        gerarCobrancasSimples(despesa, grupo, admin);

        log.debug("[finish] DespesaApplicationService - registraDespesa");
        return new DespesaResponse(despesa);
    }

    private void gerarCobrancasSimples(Despesa despesa, Grupo grupo, Usuario pagador) {
        log.info("[start] DespesaApplicationService - gerarCobrancaSimples");
        List<Cobranca> cobrancas = new ArrayList<>();
        long qtdMembros = grupo.getMembros().size();
        
        if (qtdMembros >= 1) {
            BigDecimal valorPorCabeca = despesa.getValor().divide(BigDecimal.valueOf(qtdMembros), 2, java.math.RoundingMode.HALF_UP);
            
            grupo.getMembros().forEach(membro -> {
                if (!membro.getUsuario().getId().equals(pagador.getId())) {
                    LocalDate vencimento = despesa.getDataVencimentoAvulsa() != null ? 
                                           despesa.getDataVencimentoAvulsa() : LocalDate.now().plusDays(7);
                    
                    cobrancas.add(new Cobranca(despesa, membro.getUsuario(), valorPorCabeca, StatusCobranca.PENDENTE, vencimento));
                }
            });
            cobrancaRepository.salvarVarias(cobrancas);
            log.debug("[finish] DespesaApplicationService - gerarCobrancaSimples");

        }
    }

    @Override
    @Transactional
    public void excluiDespesa(UUID grupoId, UUID despesaId, String emailUsuarioLogado) {
        log.info("[start] DespesaApplicationService - excluiDespesa");
        Despesa despesa = despesaRepository.buscaPorId(despesaId);
        despesa.setStatus(StatusDespesa.CANCELADA);
        despesaRepository.salvar(despesa);
        log.debug("[finish] DespesaApplicationService - excluiDespesa");
    }

    @Override
    @Transactional(readOnly = true)
    public List<DespesaResponse> listarDespesasDoGrupo(UUID grupoId, String emailUsuarioLogado) {
        log.info("[start] DespesaApplicationService - listarDespesasDoGrupo");
        Usuario usuario = usuarioRepository.buscaUsuarioPorEmail(emailUsuarioLogado);
        Grupo grupo = grupoRepository.buscaGrupoPorId(grupoId);

        if (grupo.buscaMembro(usuario) == null) {
            throw APIException.build(HttpStatus.FORBIDDEN, "Usuário não pertence ao grupo.");
        }

        List<Despesa> despesas = despesaRepository.findByGrupoId(grupoId);
        log.debug("[finish] DespesaApplicationService - listarDespesasDoGrupo");
        return despesas.stream()
                .map(DespesaResponse::new)
                .toList();
    }
}