package com.unisantos.clinica_api.cadastro.usuario.dto;

/**
 * Resposta do {@code POST /auth/login}.
 *
 * <p>O client guarda o {@code token} sob a chave {@code clinica.auth.token} e
 * usa o {@code user} como perfil autenticado, conforme
 * {@code client/src/core/auth/service.ts}.
 */
public record LoginResponse(String token, UsuarioResponse user) {}
