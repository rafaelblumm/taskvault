package com.taskvault.app.payload.response;

import java.time.LocalDateTime;

import com.taskvault.app.model.Jwt;

/**
 * Resposta de autenticação de usuários
 * @param token Token de usuário
 * @param expiresAt Data de expiração do token
 */
public record LoginResponse(
    String token,
    LocalDateTime expiresAt
) {

    /**
     * Cria DTO de resposta de autenticação a partir de instância de {@Code Jwt}
     * @param jwt
     * @return
     */
    public static LoginResponse from(Jwt jwt) {
        return new LoginResponse(jwt.token(), jwt.expirationDateTime());
    }

}
