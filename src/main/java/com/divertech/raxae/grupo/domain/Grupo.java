package com.divertech.raxae.grupo.domain;

import com.divertech.raxae.cobranca.domain.Despesa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;
    @NotBlank
    private String nomeGrupo;
    @NotBlank
    private String descricao;
    @NotBlank
    private String icone;
    @NotNull
    private UUID adminId;
    private LocalDateTime dataCriacao;
    @OneToMany
    @JoinColumn(name = "despesa_id")
    private List<Despesa> despesas;
    @OneToMany
    @JoinColumn(name = "membros_id")
    private List<Membro> membros;

}
