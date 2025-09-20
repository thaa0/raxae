package com.divertech.raxae.grupo.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    UUID id;
}
