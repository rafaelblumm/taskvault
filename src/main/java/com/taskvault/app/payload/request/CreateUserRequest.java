package com.taskvault.app.payload.request;

import com.taskvault.app.model.UserRole;

/**
 * Dados para criação de usuários
 * @param id ID do usuário
 * @param name Nome do usuário
 * @param email Email do usuário
 * @param role Cargo do usuário
 * @param password Senha do usuário
 */
public record CreateUserRequest(
    String id,
    String name,
    String email,
    UserRole role,
    String password
) {}
