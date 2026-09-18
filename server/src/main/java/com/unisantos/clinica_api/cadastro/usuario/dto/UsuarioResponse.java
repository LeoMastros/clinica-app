package com.unisantos.clinica_api.cadastro.usuario.dto;

import com.unisantos.clinica_api.cadastro.usuario.entity.Perfil;
import com.unisantos.clinica_api.cadastro.usuario.entity.TipoProfissional;
import com.unisantos.clinica_api.cadastro.usuario.entity.Usuario;
import java.time.LocalDate;

/**
 * Perfil do usuario devolvido pela API.
 *
 * <p>Os nomes dos campos sao exatamente os do tipo {@code User} em
 * {@code client/src/types/user.ts} — este e o unico ponto onde os nomes em
 * portugues da entidade viram os nomes que o front consome. O hash da senha
 * nunca aparece aqui.
 */
public record UsuarioResponse(
        Integer id,
        String name,
        String email,
        Perfil role,
        TipoProfissional professionalKind,
        LocalDate registrationEndDate,
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
