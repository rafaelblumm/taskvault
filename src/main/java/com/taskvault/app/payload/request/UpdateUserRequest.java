package com.taskvault.app.payload.request;

import com.taskvault.app.model.UserRole;

/**
 * Dados da atualização de usuários
 * @param name Nome do usuário
 * @param email Email do usuário
 * @param password Senha do usuário
 * @param role Cargo do usuário
 */
public record UpdateUserRequest(
        String name,
        String email,
        String password,
        UserRole role
) {
    /**
     * Busca nome do usuário
     * @return nome do usuário
     */
    public String getName() {return name;}

    /**
     * Busca endereço de e-mail do usuário
     * @return Endereço de e-mail
     */
    public String getEmail() {return email;}

    /**
     * Busca senha do usuário
     * @return senha do usuário
     */
    public String getPassword() {return password;}

    /**
     * Busca cargo do usuário
     * @return cargo do usuário
     */
    public UserRole getRole() {return role;}
}