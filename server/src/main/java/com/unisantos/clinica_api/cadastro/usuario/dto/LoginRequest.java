package com.unisantos.clinica_api.cadastro.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Corpo do {@code POST /auth/login}.
 *
 * <p>Os nomes dos campos sao os mesmos do tipo {@code Credentials} em
 * {@code client/src/core/auth/types.ts}.
 */
public record LoginRequest(
        @NotBlank(message = "Informe o e-mail") @Email(message = "E-mail invalido") String email,
        @NotBlank(message = "Informe a senha") String password) {}
