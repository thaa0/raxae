package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.application.controller.DespesaRequest;
import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.cobranca.application.controller.DespesaResponse;
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
}