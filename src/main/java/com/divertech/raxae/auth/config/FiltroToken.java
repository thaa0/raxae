package com.divertech.raxae.auth.config;

import com.divertech.raxae.auth.config.service.JwtService;
import com.divertech.raxae.auth.config.service.UsuarioDetailsService;
import com.divertech.raxae.auth.domain.ValidaConteudoAuthorizationHeader;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Component
public class FiltroToken extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UsuarioDetailsService UsuarioDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = recuperaToken(request);
            autenticaCliente(token);
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    private void autenticaCliente(String token) {
        String username = jwtService.getUsuarioByBearerToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido!"));

        var userDetails = UsuarioDetailsService.loadUserByUsername(username);
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private String recuperaToken(HttpServletRequest request) {
        log.info("[inicio] recuperaToken - extraindo o token dos cabecalhos da requisicao");
        var AuthorizationHeaderValueOpt = Optional.ofNullable(recuperaValorAuthorizationHeader(request));
        String AuthorizationHeaderValue = AuthorizationHeaderValueOpt.filter(new ValidaConteudoAuthorizationHeader())
                .orElseThrow(() -> new RuntimeException("Token inválido!"));
        log.info("[finaliza] recuperaToken - extraindo o token dos cabecalhos da requisicao");
        return AuthorizationHeaderValue; // mantém o "Bearer "
    }

    private String recuperaValorAuthorizationHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .orElseThrow(() -> new RuntimeException("Token não está presente na requisição!"));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/public/") ||
                path.contains("/v3/api-docs") ||
                path.contains("/swagger") ||
                path.contains("/swagger-ui/") ||
                path.contains("/v1/auth/cadastro") ||
                path.contains("/v1/auth/login") ||
                path.contains("/h2-console");

    }

}
