package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.application.controller.DespesaRequest;
import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.domain.StatusDespesa; // Import necessário
import com.divertech.raxae.cobranca.application.controller.DespesaResponse;
import com.divertech.raxae.cobranca.repository.DespesaRepository; // Import necessário
import com.divertech.raxae.grupo.domain.Grupo;
import com.divertech.raxae.grupo.application.repository.GrupoRepository; 
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.domain.Usuario;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; 
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DespesaApplicationService {

    private final DespesaService despesaService;
    private final UsuarioRepository usuarioRepository; 
    private final GrupoRepository grupoRepository;      
    private final DespesaRepository despesaRepository;

    public DespesaResponse registraDespesa(UUID grupoId, DespesaRequest request, String emailUsuarioLogado) {
        Usuario admin = usuarioRepository.buscaUsuarioPorEmail(emailUsuarioLogado);
        if (admin == null) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Usuário (admin) não encontrado!");
        }

        Grupo grupo = grupoRepository.buscaGrupoPorId(grupoId); 
        if (grupo == null) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Grupo não encontrado!");
        }

        if (!grupo.getAdminId().equals(admin.getId())) { 
            throw APIException.build(HttpStatus.FORBIDDEN, "Acesso negado: Somente o admin do grupo pode registrar despesas.");
        }

        Despesa despesa = despesaService.registrarDespesa(grupo, admin, request);

        return new DespesaResponse(despesa);
    }

    public void excluiDespesa(UUID grupoId, UUID despesaId, String emailUsuarioLogado) {

        Usuario admin = usuarioRepository.buscaUsuarioPorEmail(emailUsuarioLogado);
        if (admin == null) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Usuário (admin) não encontrado!");
        }
        Grupo grupo = grupoRepository.buscaGrupoPorId(grupoId);
        if (grupo == null) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Grupo não encontrado!");
        }
        if (!grupo.getAdminId().equals(admin.getId())) {
            throw APIException.build(HttpStatus.FORBIDDEN, "Acesso negado: Somente o admin do grupo pode excluir despesas.");
        }

        Despesa despesa = despesaRepository.buscaPorId(despesaId);
        if (despesa == null) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Despesa não encontrada.");
        }

        if (!despesa.getGrupo().getId().equals(grupoId)) {
            throw APIException.build(HttpStatus.FORBIDDEN, "Acesso negado: Despesa não pertence a este grupo.");
        }

        if (despesa.getStatus() == StatusDespesa.CANCELADA) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Esta despesa já está cancelada.");
        }

        despesaService.cancelarDespesa(despesa);
    }
}