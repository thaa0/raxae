package com.divertech.raxae.usuario.application.service;

import com.divertech.raxae.auth.config.service.AuthService;
import com.divertech.raxae.usuario.application.controller.InfoUsuarioResponse;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import com.divertech.raxae.usuario.application.controller.UsuarioRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Log4j2
public class UsuarioApplicationService implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encriptador;
    private final AuthService authService;

    @Override
    public void cadastrarUsuario(UsuarioRequest request) {
        log.info("[start] UsuarioApplicationService - cadastrarUsuario");
        Usuario usuario = new Usuario(request, encriptador);
        usuarioRepository.salva(usuario);
        log.debug("[finish] UsuarioApplicationService - cadastrarUsuario");
    }

    @Override
    public InfoUsuarioResponse getInfoUsuario(Usuario usuarioLogado) {
        log.info("[start] UsuarioApplicationService - getInfoUsuario");
        // Mocked values for EconomiaTotal and totalPagoNoMes
        double economiaTotal = 1500.00; // Example mocked value
        double totalPagoNoMes = 300.00; // Example mocked value
        //int numeroDeGrupos deve ser buscado no repository. A tabela membro tem a relação dos usuários com os grupos.
        int numeroDeGrupos = usuarioRepository.contaGruposDoUsuario(usuarioLogado.getId());
        InfoUsuarioResponse infoUsuarioResponse = new InfoUsuarioResponse(
                numeroDeGrupos,
                economiaTotal,
                totalPagoNoMes
        );
        log.debug("[finish] UsuarioApplicationService - getInfoUsuario");
        return infoUsuarioResponse;
    }
}
