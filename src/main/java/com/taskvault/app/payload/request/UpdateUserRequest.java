package com.taskvault.app.payload.request;

import com.taskvault.app.model.UserRole;

/**
 * Dados da atualização de usuários
 * @param name Nome do usuário
 * @param email Email do usuário
 * @param role Cargo do usuário
 * @param password Senha do usuário
 */
public record UpdateUserRequest(
    String name,
    String email,
    UserRole role,
    String password
) {}