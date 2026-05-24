package com.taskvault.app.payload.request;

/**
 * Dados da requisição de login de usuário
 * @param user Nome de usuário (ID)
 * @param pass Senha do usuário
 */
public record LoginRequest(String user, String pass) {}
