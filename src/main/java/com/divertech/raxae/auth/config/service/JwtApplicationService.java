package com.divertech.raxae.auth.config.service;

import com.divertech.raxae.usuario.domain.Usuario;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@Log4j2
public class JwtApplicationService implements JwtService {

    @Value("${raxae.jwt.expiracao}")
    private String expiracao;

    @Value("${raxae.jwt.chave}")
    private String chave;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(chave.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String gerarToken(Authentication authentication) {
        return gerarToken((Usuario) authentication.getPrincipal());
    }

    @Override
    public String gerarToken(Usuario usuario) {
        log.info("[inicio] TokenService - criação de token");
        Date agora = new Date();
        Date expiracaoToken = Date.from(LocalDateTime.now()
                .plus(java.time.Duration.ofMillis(Long.parseLong(expiracao)))
                .atZone(ZoneId.systemDefault())
                .toInstant());

        String token = Jwts.builder()
                .setIssuer("API Raxae")
                .setSubject(usuario.getEmail())
                .claim("nomeCompleto", usuario.getNomeCompleto())
                .setIssuedAt(agora)
                .setExpiration(expiracaoToken)
                .signWith(getSecretKey())
                .compact();

        log.info("[finaliza] TokenService - criação de token");
        return token;
    }

    @Override
    public Optional<String> getUsuario(String token) {
        try {
            log.info("[inicio] TokenService - extração do ID do Token");
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(claims.getSubject());
        } catch (ExpiredJwtException ex) {
            Claims claims = ex.getClaims();
            log.info("[finaliza] TokenService - extração do ID do Token");
            return Optional.of(claims.getSubject());
        }
    }

    @Override
    public Optional<String> getUsuarioByBearerToken(String token) {
        log.info("[inicio] TokenService - getUsuarioByBearerToken");
        if (token == null || token.length() < 7 || !token.startsWith("Bearer ")) {
            log.warn("Token inválido ou ausente o prefixo 'Bearer '");
            return Optional.empty();
        }
        String bearerToken = token.substring(7);
        log.info("[finaliza] TokenService - getUsuarioByBearerToken");
        return this.getUsuario(bearerToken);
    }
}
