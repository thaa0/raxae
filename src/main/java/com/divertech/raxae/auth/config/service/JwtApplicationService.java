package com.divertech.raxae.auth.config.service;

import com.divertech.raxae.usuario.domain.Usuario;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
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

    public String gerarToken(Authentication authentication) {
        return gerarToken((Usuario) authentication.getPrincipal());
    }

    public String gerarToken(Usuario usuario) {
        log.info("[inicio] TokenService - criação de token");
        String token = Jwts.builder()
                .setIssuer("API Raxae")
                .setSubject(usuario.getEmail())
                .claim("nomeCompleto", usuario.getNomeCompleto())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now()
                        .plusMinutes(Long.parseLong(expiracao))
                        .atZone(ZoneId.systemDefault())
                        .toInstant()))
                .signWith(SignatureAlgorithm.HS256, chave)
                .compact();
        log.info("[finaliza] TokenService - criação de token");
        return token;
    }

    public Optional<String> getUsuario(String token) {
        try {
            log.info("[inicio] TokenService - extração do ID do Token");
            var claims = Jwts.parser().setSigningKey(chave).parseClaimsJws(token).getBody();
            return Optional.of(claims.getSubject());
        } catch (ExpiredJwtException ex) {
            var claims = ex.getClaims();
            log.info("[finaliza] TokenService - extração do ID do Token");
            return Optional.of(claims.getSubject());
        }
    }

    public Optional<String> getUsuarioByBearerToken(String token) {
        log.info("[inicio] TokenService - getUsuarioByBearerToken");
        String bearerToken = token.substring(7,token.length());
        log.info(token);
        log.info("[finaliza] TokenService - getUsuarioByBearerToken");
        return this.getUsuario(bearerToken);
    }
}
