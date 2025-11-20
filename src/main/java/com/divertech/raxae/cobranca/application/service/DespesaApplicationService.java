package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.application.controller.DespesaRequest;
import com.divertech.raxae.cobranca.application.controller.DespesaResponse;
import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.domain.StatusDespesa;
import com.divertech.raxae.cobranca.domain.TipoRecorrencia;
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

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class DespesaApplicationService implements DespesaService {

    private final UsuarioRepository usuarioRepository; 
    private final GrupoRepository grupoRepository;      
    private final DespesaRepository despesaRepository;
    private final GeracaoAutomaticaCobrancaService geracaoCobrancaService;

    @Override
    @Transactional
    public DespesaResponse registraDespesa(UUID grupoId, DespesaRequest request, String emailUsuarioLogado) {
        log.info("[start] DespesaApplicationService - registraDespesa");
        Usuario admin = usuarioRepository.buscaUsuarioPorEmail(emailUsuarioLogado);
        Grupo grupo = grupoRepository.buscaGrupoPorId(grupoId);
        validaPermissao(grupo,admin);
        Despesa despesa = despesaRepository.salvar(new Despesa(grupo, admin, request));
        validaTipoRecorrencia(despesa);
        log.debug("[finish] DespesaApplicationService - registraDespesa");
        return new DespesaResponse(despesa);
    }

    private void validaTipoRecorrencia(Despesa despesa) {
        if(despesa.getTipoRecorrencia() == TipoRecorrencia.UNICA){
            LocalDate dataVencimento = YearMonth.now().atDay(Math.min(despesa.getDiaVencimento(), YearMonth.now().lengthOfMonth()));
            geracaoCobrancaService.gerarCobrancasParaDespesa(despesa, dataVencimento);
        }
    }

    @Override
    @Transactional
    public void excluiDespesa(UUID grupoId, UUID despesaId, String emailUsuarioLogado) {
        log.info("[start] DespesaApplicationService - excluiDespesa");
        Despesa despesa = despesaRepository.buscaPorId(despesaId);
        validaPermissao(grupoRepository.buscaGrupoPorId(grupoId), usuarioRepository.buscaUsuarioPorEmail(emailUsuarioLogado));
        verificaSeDespesaPertenceAoGrupo(grupoId, despesa);
        despesa.setStatus(StatusDespesa.CANCELADA);
        despesaRepository.salvar(despesa);
        log.debug("[finish] DespesaApplicationService - excluiDespesa");

    }

    private static void verificaSeDespesaPertenceAoGrupo(UUID grupoId, Despesa despesa) {
        if (!despesa.getGrupo().getId().equals(grupoId)) {
            throw APIException.build(HttpStatus.FORBIDDEN, "Acesso negado: Despesa não pertence a este grupo.");
        }
    }

    private static void validaPermissao(Grupo grupo, Usuario admin) {
        if (!grupo.getAdminId().equals(admin.getId())) {
            throw APIException.build(HttpStatus.FORBIDDEN, "Acesso negado: Somente o admin tem permissão para alteracoes.");
        }
    }
}