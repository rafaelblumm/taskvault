package com.taskvault.app.user;

/**
 * Resposta de requisições que retornam um usuário.
 * Contém somente os campos necessários.
 * @param id Username
 * @param name Nome de usuário
 * @param email Endereço de e-mail do usuário
 * @param role Nível de permissão do usuário
 */
public record UserResponse(
    String id,
    String name,
    String email,
    UserRole role
) {

    /**
     * Converte instância de {@Code User} para {@Code UserResponse}
     * @param user Dados do usuário
     * @return
     */
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
    }

};
