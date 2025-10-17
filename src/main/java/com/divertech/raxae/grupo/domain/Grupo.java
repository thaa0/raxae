package com.divertech.raxae.grupo.domain;

import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.grupo.application.controller.GrupoEditaRequest;
import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;
import com.divertech.raxae.handler.APIException;
import com.divertech.raxae.usuario.domain.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;

    @NotBlank
    @Column(unique = true)
    private String nomeGrupo;

    @NotBlank
    private String descricao;

    @NotBlank
    private String icone;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Usuario administrador;

    private LocalDateTime dataCriacao;

    @OneToMany
    @JoinColumn(name = "grupo_id")
    private List<Despesa> despesas = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Membro> membros = new HashSet<>();

    public Grupo(GrupoNovoRequest grupoRequest) {
        this.nomeGrupo = grupoRequest.getNomeGrupo();
        this.descricao = grupoRequest.getDescricao();
        this.icone = grupoRequest.getIcone();
        this.dataCriacao = LocalDateTime.now();
    }

    public void atualizaInformacoes(GrupoEditaRequest grupoEditaRequest) {
        if (grupoEditaRequest.getNomeGrupo() != null && !grupoEditaRequest.getNomeGrupo().isBlank()) {
            this.nomeGrupo = grupoEditaRequest.getNomeGrupo();
        }
        if (grupoEditaRequest.getDescricao() != null && !grupoEditaRequest.getDescricao().isBlank()) {
            this.descricao = grupoEditaRequest.getDescricao();
        }
        if (grupoEditaRequest.getIcone() != null && !grupoEditaRequest.getIcone().isBlank()) {
            this.icone = grupoEditaRequest.getIcone();
        }
    }

    public void removeMembro(UUID idDoMembro) {
        this.membros.removeIf(membro -> membro.getUsuario().getId().equals(idDoMembro));
    }

    public void adicionaNovoMembro(Usuario usuario) {
        boolean jaEhMembro = this.membros.stream()
            .anyMatch(membro -> membro.getUsuario().getId().equals(usuario.getId()));
            
        if (jaEhMembro) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Este usuário já faz parte do grupo.");
        }
        
        Membro novoMembro = Membro.builder()
            .usuario(usuario)
            .grupo(this)
            .status(StatusParticipacao.PENDENTE)
            .build();
            
        this.membros.add(novoMembro);
    }
    
    public boolean isAdmin(String email) {
        if (this.administrador == null) return false;
        return this.administrador.getEmail().equalsIgnoreCase(email);
    }
    
    public UUID getAdminId() {
        if (this.administrador == null) return null;
        return this.administrador.getId();
    }

    public Membro buscaMembro(Usuario usuario) {
        return this.membros.stream() .filter(membro -> membro.getUsuario().getId().equals(usuario.getId())) .findFirst() .orElse(null);
    }
}