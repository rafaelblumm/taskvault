package com.taskvault.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.taskvault.app.error.UserAlreadyExistsException;
import com.taskvault.app.model.User;
import com.taskvault.app.payload.response.UserResponse;
import com.taskvault.app.repository.UserRepository;
import com.taskvault.app.security.service.PasswordHasherService;

/** Controller do endpoint de gerenciamento de usuários */
@RestController
public class UserController {

    /** Camada de acesso a tabela de usuários */
    @Autowired
    private UserRepository userRepository;

    /**
     * Cria novo usuário
     * @param user Dados do usuário sendo criado
     * @return Usuário criado
     * @throws UserAlreadyExistsException Se usuário já existir na base de dados
     */
    @PostMapping(path="/user")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody User user) {
        if (userExist(user)) {
            throw new UserAlreadyExistsException();
        }

        user.setPassword(PasswordHasherService.hash(user.getPassword()));
        userRepository.save(user);

        return UserResponse.from(user);
    }

    /**
     * Verifica se usuário já existe na base de dados. Valida campos chave
     * @param user Usuário
     * @return Se usuário já existe
     */
    private boolean userExist(User user) {
        return userRepository.findById(user.getId()).isPresent() ||
                userRepository.findByEmail(user.getEmail()).isPresent();
    }

}
