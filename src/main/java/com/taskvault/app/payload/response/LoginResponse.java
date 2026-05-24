package com.taskvault.app.payload.response;

/**
 * Resposta de autenticação de usuários
 * @param token Token de usuário
 */
public record LoginResponse(String token) {}
