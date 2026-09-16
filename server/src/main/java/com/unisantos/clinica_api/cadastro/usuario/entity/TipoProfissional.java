package com.unisantos.clinica_api.cadastro.usuario.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Vinculo do profissional com a clinica.
 *
 * <p>Espelha {@code ProfessionalKind} de {@code client/src/types/user.ts}.
 * E nulo para quem nao e profissional de psicologia, como a secretaria.
 *
 * <p>O prazo de registro de cada tipo (estagiario ate o fim do semestre,
 * recem-formado ate o fim do ano) e regra de servico e sera tratado no
 * cadastro, nao aqui.
 */
public enum TipoProfissional {

    STUDENT_INTERN("student_intern"),
    RECENT_GRADUATE("recent_graduate"),
    STAFF("staff");

    private final String valor;

    TipoProfissional(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static TipoProfissional deValor(String valor) {
        for (TipoProfissional tipo : values()) {
            if (tipo.valor.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de profissional invalido: " + valor);
    }
}
