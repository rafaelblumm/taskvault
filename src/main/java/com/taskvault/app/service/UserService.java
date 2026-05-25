package com.taskvault.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskvault.app.error.UserAlreadyExistsException;
import com.taskvault.app.model.User;
import com.taskvault.app.repository.UserRepository;

/** Serviço de gestão de usuários */
@Service
public class UserService {

    /** Camada de acesso a tabela de usuários */
    @Autowired
    private UserRepository userRepository;

    /** Codificador de senha de usuários */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Cria novo usuário
     * @param user Dados do usuário sendo criado
     * @return Usuário criado
     * @throws UserAlreadyExistsException Se usuário já existir na base de dados
     */
    public User createUser(User user) throws UserAlreadyExistsException{
        if (userExist(user)) {
            throw new UserAlreadyExistsException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Verifica se usuário já existe na base de dados. Valida campos chave
     * @param user Usuário
     * @return Se usuário já existe
     */
    private boolean userExist(User user) {
        return userRepository.existsById(user.getId()) ||
                userRepository.existsByEmail(user.getEmail());
    }

}
