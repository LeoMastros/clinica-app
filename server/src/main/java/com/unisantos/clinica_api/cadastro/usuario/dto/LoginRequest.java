package com.unisantos.clinica_api.cadastro.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Corpo do {@code POST /auth/login}.
 *
 * <p>Os nomes dos campos sao os mesmos do tipo {@code Credentials} em
 * {@code client/src/core/auth/types.ts}.
 */
@Schema(description = "Credenciais de acesso.")
public record LoginRequest(
        @Schema(
                        description = "E-mail cadastrado do usuario.",
                        example = "maria.souza@unisantos.br",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "Informe o e-mail")
                @Email(message = "E-mail invalido")
                String email,
        @Schema(
                        description = "Senha em texto puro. Trafega apenas no corpo da requisicao e nunca e devolvida.",
                        example = "minha-senha-secreta",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "Informe a senha")
                String password) {}
