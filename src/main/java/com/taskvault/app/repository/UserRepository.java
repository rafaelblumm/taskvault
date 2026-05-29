package com.taskvault.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.taskvault.app.model.User;

/** Camada de acesso aos dados da tabela de usuários */
public interface UserRepository extends CrudRepository<User, String> {

    /**
     * Identifica se usuário existe pelo e-mail (chave única)
     * @param email E-mail do usuário
     * @return Se usuário existe
     */
    public boolean existsByEmail(String email);

    /**
     * Identifica se usuário existe por username (chave primária) ou e-mail (chave única)
     * @param id Username
     * @param email E-mail
     * @return Se usuário existe
     */
    public boolean existsByIdOrEmail(String id, String email);

}
