package com.divertech.raxae.usuario.domain;

import com.divertech.raxae.plano.domain.TipoPlano;
import com.divertech.raxae.usuario.application.controller.UsuarioRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;
    @NotBlank(message = "Campo não pode ser em branco")
    private String nomeCompleto;
    @Email
    @Column(unique = true)
    private String email;
    @NotNull
    @Getter(AccessLevel.NONE)
    @Size(max = 60)
    private String senha;
    private TipoPlano tipo;
    @NotBlank(message = "Campo não pode ser em branco")
    private String whatsapp;
    private LocalDateTime momentoCriacao;

    public Usuario(UsuarioRequest usuarioRequest, BCryptPasswordEncoder passwordEncoder) {
        this.whatsapp = usuarioRequest.getWhatsapp();
        this.senha = passwordEncoder.encode(usuarioRequest.getSenha());;
        this.email = usuarioRequest.getEmail();
        this.nomeCompleto = usuarioRequest.getNomeCompleto();
        this.tipo = TipoPlano.GRATUITO;
        this.momentoCriacao = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    @Override
    public String getPassword() {
        return this.senha;
    }
    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}