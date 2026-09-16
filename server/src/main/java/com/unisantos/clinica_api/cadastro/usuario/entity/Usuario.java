package com.unisantos.clinica_api.cadastro.usuario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Usuario do sistema: profissionais de psicologia e pessoal administrativo.
 *
 * <p>O schema e criado pela migration {@code V20__cadastro_usuario.sql}. Esta
 * classe apenas mapeia aquela tabela — com {@code ddl-auto=validate}, o
 * Hibernate confere o mapeamento no boot e falha se os dois divergirem.
 *
 * <p>Usuario nunca e excluido. Desligamento e feito por {@code ativo = false},
 * pela regra de retencao do CFP/CRP descrita no PR #13.
 */
@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    /** Hash BCrypt. A senha em texto puro nunca chega a esta entidade. */
    @Column(name = "senha_hash", nullable = false, length = 100)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false, length = 20)
    private Perfil perfil;

    /** Nulo para quem nao e profissional de psicologia. */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_profissional", length = 20)
    private TipoProfissional tipoProfissional;

    /** Fim do vinculo do profissional. Nulo enquanto nao houver prazo definido. */
    @Column(name = "data_fim_registro")
    private LocalDate dataFimRegistro;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    void aoCriar() {
        this.dataCriacao = LocalDateTime.now();
    }

    @PreUpdate
    void aoAtualizar() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}
