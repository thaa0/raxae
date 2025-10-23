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
        double economiaTotal = 1500.00; // Mockado
        double totalPagoNoMes = 300.00; // Mockado
        int numeroDeGrupos = usuarioRepository.contaGruposDoUsuario(usuarioLogado.getId());
        log.debug("DEBUG: Número de grupos do usuário: {}", numeroDeGrupos);
        InfoUsuarioResponse infoUsuarioResponse = new InfoUsuarioResponse(
                numeroDeGrupos,
                economiaTotal,
                totalPagoNoMes
        );
        log.debug("DEBUG: Economia total: {}, Total pago no mês: {}", economiaTotal, totalPagoNoMes);
        log.debug("[finish] UsuarioApplicationService - getInfoUsuario");
        return infoUsuarioResponse;
    }
}
