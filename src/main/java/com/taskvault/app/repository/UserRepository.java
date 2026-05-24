package com.taskvault.app.repository;

import java.util.Optional;

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

}
