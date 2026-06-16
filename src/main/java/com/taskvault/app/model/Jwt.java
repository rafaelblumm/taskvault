package com.taskvault.app.model;

import java.time.LocalDateTime;

/**
 * Modelo de token JWT para autenticação de usuários
 * @param token Token de usuário
 * @param expirationDateTime Data e hora de expiração do token
 */
public record Jwt(
    String token,
    LocalDateTime expirationDateTime
) {}
