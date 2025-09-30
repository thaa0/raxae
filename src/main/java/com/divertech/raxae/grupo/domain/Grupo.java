package com.divertech.raxae.grupo.domain;

import com.divertech.raxae.cobranca.domain.Despesa;
import com.divertech.raxae.grupo.application.controller.GrupoEditaRequest;
import com.divertech.raxae.grupo.application.controller.GrupoNovoRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Column(unique = true)
    private String nomeGrupo;
    
    @NotBlank
    private String descricao;
    
    @NotBlank
    private String icone;
    
    @NotNull
    private UUID adminId;
    
    private boolean ativo = true;
    
    private LocalDateTime dataCriacao;
    
    @OneToMany
    @JoinColumn(name = "grupo_id")
    private List<Despesa> despesas;
    
    @OneToMany
    @JoinColumn(name = "grupo_id")
    private List<Membro> membros;

    public Grupo(GrupoNovoRequest grupoRequest) {
        this.nomeGrupo = grupoRequest.getNomeGrupo();
        this.descricao = grupoRequest.getDescricao();
        this.icone = grupoRequest.getIcone();
        this.adminId = UUID.fromString(grupoRequest.getIdUserAdmin());
        this.dataCriacao = LocalDateTime.now();
        this.despesas = new ArrayList<>();
        this.membros = new ArrayList<>();
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


    public void desativa() {
        this.ativo = false;
    }

    public Grupo validaAdmin(UUID idUsuario) {
        if (!this.adminId.equals(idUsuario)) {
            throw new RuntimeException("Usuário não é administrador do grupo.");
        }
        return this;
    }
}
