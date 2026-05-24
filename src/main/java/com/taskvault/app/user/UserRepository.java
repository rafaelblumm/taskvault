package com.taskvault.app.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

/** Camada de acesso aos dados da tabela de usuários */
public interface UserRepository extends CrudRepository<User, String> {

    /**
     * Busca usuário pelo e-mail (chave única)
     * @param email E-mail sendo buscado
     * @return Se houver usuário com o e-mail, retorna {@Code User} encapsulado
     * em um {@Code Optional}. Se não existir, retorna {@Code Optional.empty}
     */
    public Optional<User> findByEmail(String email);

}
