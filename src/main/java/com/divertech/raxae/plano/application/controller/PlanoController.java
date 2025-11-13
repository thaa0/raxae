package com.divertech.raxae.plano.application.controller;

import com.divertech.raxae.plano.application.service.PlanoService;
import com.divertech.raxae.usuario.domain.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/planos")
@RequiredArgsConstructor
@Log4j2
public class PlanoController {

    private final PlanoService planoService; 

    @GetMapping
    public List<PlanoResponse> listarPlanosDisponiveis() {
        log.info("[start] PlanoController - listarPlanosDisponiveis");
        List<PlanoResponse> planos = planoService.listarPlanos();
        log.info("[finish] PlanoController - listarPlanosDisponiveis");
        return planos;
    }
    
    @PatchMapping("/atualizar")
    public AdesaoResponse atualizarPlanoDoUsuario(
            
            @AuthenticationPrincipal Usuario usuarioLogado,

            @RequestBody @Valid AtualizarPlanoRequest request) {
        
        log.info("[start] PlanoController - atualizarPlanoDoUsuario");

        AdesaoResponse adesaoResponse = planoService.atualizarPlano(usuarioLogado, request);
        log.info("[finish] PlanoController - atualizarPlanoDoUsuario");
        return adesaoResponse;
    }
}