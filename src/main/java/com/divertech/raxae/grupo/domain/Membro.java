package com.divertech.raxae.grupo.domain;

import com.divertech.raxae.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Grupo grupo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusParticipacao status;

    public Membro(Usuario usuario, Grupo grupo) {
        this.usuario = usuario;
        this.grupo = grupo;
        this.status = StatusParticipacao.ATIVO;
    }
}