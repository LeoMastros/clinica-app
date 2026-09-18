package com.unisantos.clinica_api.cadastro.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resposta do {@code POST /auth/login}.
 *
 * <p>O client guarda o {@code token} sob a chave {@code clinica.auth.token} e
 * usa o {@code user} como perfil autenticado, conforme
 * {@code client/src/core/auth/service.ts}.
 */
@Schema(description = "Token de acesso e o perfil de quem entrou.")
public record LoginResponse(
        @Schema(
                        description = "JWT a ser enviado no cabecalho Authorization, no formato \"Bearer <token>\". Validade padrao de 8 horas.",
                        example = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjbGluaWNhLWFwaSJ9.assinatura")
                String token,
        @Schema(description = "Perfil do usuario que acabou de autenticar.") UsuarioResponse user) {}
