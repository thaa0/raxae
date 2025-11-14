package com.divertech.raxae.plano.domain;

import com.divertech.raxae.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Adesao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_id", nullable = false)
    private Plano plano;

    @Column(nullable = false, unique = true)
    private UUID idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAdesao statusAdesao;

    @Column(nullable = false)
    private int diaExpiracao;

    @Column(nullable = false)
    private LocalDateTime momentoAdesao;

    private Adesao(Usuario usuario, Plano plano) {
        this.plano = plano;
        this.idUsuario = usuario.getId();
        this.statusAdesao = StatusAdesao.ATIVO;
        this.momentoAdesao = LocalDateTime.now();
        this.diaExpiracao = LocalDateTime.now().plusYears(100).getDayOfMonth(); 
    }

    public static Adesao criarAdesaoInicial(Usuario usuario, Plano planoGratuito) {
        return new Adesao(usuario, planoGratuito);
    }
    
    public void atualizarPlano(Plano novoPlano) {
        this.plano = novoPlano;
        this.statusAdesao = StatusAdesao.ATIVO; 
        this.momentoAdesao = LocalDateTime.now();
    }
    
    public void setStatus(StatusAdesao status) {
        this.statusAdesao = status;
    }
}