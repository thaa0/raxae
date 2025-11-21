package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.application.controller.PagamentoResponse;
import jakarta.validation.constraints.Email;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PagamentoService {
    PagamentoResponse registrarPagamentoComComprovante(UUID expenseId, MultipartFile comprovante, String emailUsuarioLogado);
    byte[] buscarComprovantePorCobrancaId(UUID cobrancaId, @Email String email);
}

