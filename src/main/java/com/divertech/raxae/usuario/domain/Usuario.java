package com.divertech.raxae.usuario.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;
    @NotBlank(message = "Campo não pode ser em branco")
    private String nomeCompleto;
    @NotBlank(message = "Campo não pode ser em branco")
    private String email;
    private UUID idPlano;
    @NotBlank(message = "Campo não pode ser em branco")
    private String whatsapp;
    private LocalDateTime dataCriacao;

}