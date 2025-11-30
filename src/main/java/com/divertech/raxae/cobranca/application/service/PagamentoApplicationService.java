package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.application.controller.PagamentoResponse;
import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.domain.Pagamento;
import com.divertech.raxae.cobranca.domain.StatusCobranca;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
import com.divertech.raxae.cobranca.repository.DespesaRepository;
import com.divertech.raxae.cobranca.repository.PagamentoRepository;
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.grupo.domain.Membro;
import com.divertech.raxae.grupo.domain.StatusParticipacao;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class PagamentoApplicationService implements PagamentoService {

    private final DespesaRepository despesaRepository;
    private final CobrancaRepository cobrancaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public PagamentoResponse registrarPagamentoComComprovante(UUID expenseId, MultipartFile comprovante, String emailUsuarioLogado) {
        log.info("[start] PagamentoApplicationService - registrarPagamentoComComprovante para despesa: {}", expenseId);
        
        if (comprovante == null || comprovante.isEmpty()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Comprovante é obrigatório");
        }

        if (!isValidImageFile(comprovante)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Apenas arquivos de imagem são aceitos (JPEG, PNG, GIF)");
        }

        Despesa despesa = despesaRepository.buscaPorId(expenseId);
        if (despesa == null) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Despesa não encontrada");
        }

        Usuario usuario = usuarioRepository.buscaUsuarioPorEmail(emailUsuarioLogado);
        if (usuario == null) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        validarUsuarioNoGrupo(despesa.getGrupo(), usuario);

        Cobranca cobranca = cobrancaRepository.buscarPorDespesaIdEUsuarioId(expenseId, usuario.getId());
        if (cobranca == null) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Cobrança não encontrada para esta despesa e usuário");
        }

        if (cobranca.getStatus() == StatusCobranca.PAGA) {
            throw APIException.build(HttpStatus.CONFLICT, "Esta cobrança já foi paga");
        }

        Pagamento pagamentoExistente = pagamentoRepository.buscarPorCobrancaId(cobranca.getId());
        if (pagamentoExistente != null) {
            throw APIException.build(HttpStatus.CONFLICT, "Já existe um pagamento registrado para esta cobrança");
        }

        byte[] comprovanteBytes = converterParaByteArray(comprovante);
        Pagamento pagamento = Pagamento.criar(cobranca, cobranca.getValor(), comprovanteBytes);
        pagamentoRepository.salvar(pagamento);

        cobranca.setStatus(StatusCobranca.PAGA);
        cobranca.setDataPagamento(java.time.LocalDate.now());
        cobrancaRepository.salvarVarias(java.util.List.of(cobranca));

        log.info("[finish] PagamentoApplicationService - registrarPagamentoComComprovante");
        return new PagamentoResponse(pagamento);
    }

    private void validarUsuarioNoGrupo(Grupo grupo, Usuario usuario) {
        if (grupo.getAdminId() != null && grupo.getAdminId().equals(usuario.getId())) {
            return;
        }
        
        Membro membro = grupo.buscaMembro(usuario);
        if (membro == null || membro.getStatus() != StatusParticipacao.ATIVO) {
            throw APIException.build(HttpStatus.FORBIDDEN, "Usuário não pertence a este grupo");
        }
    }

    private boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/jpg")
        );
    }

    private byte[] converterParaByteArray(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            log.error("Erro ao converter arquivo para byte array: {}", e.getMessage());
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar o comprovante");
        }
    }


    @Override
    public byte[] buscarComprovantePorCobrancaId(UUID cobrancaId, String email) {
        log.info("[start] PagamentoApplicationService - buscarComprovantePorCobrancaId para cobranca: {}", cobrancaId);

        Cobranca cobranca = cobrancaRepository.buscaPorId(cobrancaId);
        Usuario usuario = usuarioRepository.buscaUsuarioPorEmail(email);
        validarUsuarioNoGrupo(cobranca.getDespesa().getGrupo(), usuario);
        Pagamento pagamento = pagamentoRepository.buscarPorCobrancaId(cobrancaId);

        if (pagamento.getComprovante() == null || pagamento.getComprovante().length == 0) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Comprovante não encontrado");
        }

        log.info("[finish] PagamentoApplicationService - buscarComprovantePorCobrancaId");
        return pagamento.getComprovante();
    }
}

