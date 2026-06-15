package com.taskvault.app.payload.response;

import java.time.LocalDateTime;

/**
 * Resposta de autenticação de usuários
 * @param token Token de usuário
 * @param expiresAt Data de expiração do token
 */
public record LoginResponse(
    String token,
    LocalDateTime expiresAt
) {}
