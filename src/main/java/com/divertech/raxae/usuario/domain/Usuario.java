package com.divertech.raxae.usuario.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;
}