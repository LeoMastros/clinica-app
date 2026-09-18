package com.unisantos.clinica_api.cadastro.usuario.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Perfil de acesso do usuario.
 *
 * <p>Os tres perfis sao os mesmos que o front ja usa em
 * {@code client/src/types/permissions.ts}. O nome da constante e gravado no
 * banco pelo JPA; {@link #getValor()} e o texto trocado com o front no JSON.
 * Manter a conversao aqui dentro evita uma camada de traducao separada, que
 * poderia sair de sincronia com a interface.
 */
public enum Perfil {

    ADMIN("admin"),
    PSYCHOLOGIST("psychologist"),
    SECRETARY("secretary");

    private final String valor;

    Perfil(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static Perfil deValor(String valor) {
        for (Perfil perfil : values()) {
            if (perfil.valor.equalsIgnoreCase(valor)) {
                return perfil;
            }
        }
        throw new IllegalArgumentException("Perfil invalido: " + valor);
    }
}
