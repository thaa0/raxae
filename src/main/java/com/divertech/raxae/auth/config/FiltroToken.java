package com.divertech.raxae.auth.config;

import br.com.divertech.divertfest.credencial.domain.Credencial;
import com.divertech.raxae.auth.config.service.AuthService;
import com.divertech.raxae.auth.config.service.JwtService;
import com.divertech.raxae.auth.domain.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class FiltroToken extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AuthService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("[start] FiltroToken - doFilterInternal");
        String token = recuperaToken(request);
        autenticaCliente(token);
        log.debug("[finish] FiltroToken - doFilterInternal");
        filterChain.doFilter(request, response);
    }

    private String recuperaToken(HttpServletRequest request) {
        log.info("[inicio] recuperaToken - extraindo o token dos cabecalhos da requisicao");
        var AuthorizationHeaderValueOpt = Optional.ofNullable(recuperaValorAuthorizationHeader(request));
        String AuthorizationHeaderValue = AuthorizationHeaderValueOpt.filter(new com.divertech.raxae.auth.config.ValidaConteudoAuthorizationHeader())
                .orElseThrow(() -> new RuntimeException("Token inválido!"));
        log.info("[finaliza] recuperaToken - extraindo o token dos cabecalhos da requisicao");
        return AuthorizationHeaderValue.substring(7);
    }

    private String recuperaValorAuthorizationHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .orElseThrow(() -> new RuntimeException("Token não está presente na requisição!"));
    }

    private void autenticaCliente(String token) {
        log.info("[inicio] autenticacaoCliente - utilizando token válido para autenticar o usuário");
        Token credencial = recuperaUsuario(token);
        var authenticationToken = new UsernamePasswordAuthenticationToken(credencial, null, credencial.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("[finaliza] autenticacaoCliente - utilizando token válido para autenticar o usuário");
    }

    private Credencial recuperaUsuario(String token) {
        String usuario = jwtService.getUsuarioByBearerToken(token).orElseThrow(()-> new RuntimeException("O Token enviado está inválido. Tente novamente."));
        return authService.buscaCredencialPorUsuario(usuario);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.contains("/public/")||path.contains("/swagger-ui/");
    }

}
