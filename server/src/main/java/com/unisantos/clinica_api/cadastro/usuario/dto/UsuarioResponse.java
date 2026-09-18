package com.unisantos.clinica_api.cadastro.usuario.dto;

import com.unisantos.clinica_api.cadastro.usuario.entity.Perfil;
import com.unisantos.clinica_api.cadastro.usuario.entity.TipoProfissional;
import com.unisantos.clinica_api.cadastro.usuario.entity.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * Perfil do usuario devolvido pela API.
 *
 * <p>Os nomes dos campos sao exatamente os do tipo {@code User} em
 * {@code client/src/types/user.ts} — este e o unico ponto onde os nomes em
 * portugues da entidade viram os nomes que o front consome. O hash da senha
 * nunca aparece aqui.
 */
@Schema(description = "Perfil do usuario autenticado. Nunca inclui senha nem hash.")
public record UsuarioResponse(
        @Schema(description = "Identificador do usuario.", example = "1") Integer id,
        @Schema(description = "Nome completo.", example = "Maria de Souza") String name,
        @Schema(description = "E-mail de acesso, unico no sistema.", example = "maria.souza@unisantos.br")
                String email,
        @Schema(
                        description = "Perfil de acesso, que define o que o usuario pode ver e fazer.",
                        example = "psychologist",
                        allowableValues = {"admin", "psychologist", "secretary"})
                Perfil role,
        @Schema(
                        description = "Vinculo do profissional com a clinica. Nulo para quem nao e profissional de psicologia, como a secretaria.",
                        example = "student_intern",
                        allowableValues = {"student_intern", "recent_graduate", "staff"},
                        nullable = true)
                TipoProfissional professionalKind,
        @Schema(
                        description = "Data em que o vinculo do profissional se encerra. Nula enquanto nao houver prazo definido.",
                        example = "2026-11-30",
                        nullable = true)
                LocalDate registrationEndDate,
        @Schema(
                        description = "Situacao do usuario. Usuario nunca e excluido: o desligamento e feito com false.",
                        example = "true")
                boolean active) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getTipoProfissional(),
                usuario.getDataFimRegistro(),
                usuario.isAtivo());
    }
}
