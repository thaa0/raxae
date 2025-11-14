package com.divertech.raxae.plano.application.service;

import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.plano.application.controller.AdesaoResponse;
import com.divertech.raxae.plano.application.controller.AtualizarPlanoRequest;
import com.divertech.raxae.plano.application.controller.PlanoResponse;
import com.divertech.raxae.plano.application.repository.AdesaoRepository;
import com.divertech.raxae.plano.application.repository.PlanoRepository;
import com.divertech.raxae.plano.domain.Adesao;
import com.divertech.raxae.plano.domain.Plano;

import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PlanoApplicationService implements PlanoService {

    private final PlanoRepository planoRepository;
    private final AdesaoRepository adesaoRepository;

    @Override
    public List<PlanoResponse> listarPlanos() {
        log.info("[start] PlanoApplicationService - listarPlanos");
        List<PlanoResponse> planos = planoRepository.buscaTodos().stream()
                .map(PlanoResponse::new)
                .collect(Collectors.toList());
        log.info("[finish] PlanoApplicationService - listarPlanos");
        return planos;
    }

    @Override
    @Transactional
    public AdesaoResponse atualizarPlano(Usuario usuarioLogado, AtualizarPlanoRequest request) {
        log.info("[start] PlanoApplicationService - atualizarPlano");
        
        if (usuarioLogado == null) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }
        
        Adesao adesaoAtual = adesaoRepository.buscaPorUsuarioId(usuarioLogado.getId());

        Plano novoPlano = planoRepository.buscaPorId(request.getPlanoId());

        adesaoAtual.atualizarPlano(novoPlano);

        Adesao adesaoSalva = adesaoRepository.salvar(adesaoAtual);

        log.info("[finish] PlanoApplicationService - atualizarPlano");
        return new AdesaoResponse(adesaoSalva);
    }
}