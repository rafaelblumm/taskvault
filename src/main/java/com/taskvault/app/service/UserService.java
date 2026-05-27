package com.taskvault.app.service;

import com.taskvault.app.payload.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskvault.app.error.UserAlreadyExistsException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.model.User;
import com.taskvault.app.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

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
    public User createUser(User user) throws UserAlreadyExistsException {
        if (userExists(user)) throw new UserAlreadyExistsException();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Atualiza informações de um usuário
     * @param userFromDataBase Usuário a ser alterado
     * @param updateUser Dados alterados do usuário
     */
    public void updateUser(User userFromDataBase,
                           UpdateUserRequest updateUser) throws UserNotFoundException {
        if (userRepository.existsByEmail(updateUser.getEmail()) &&
                !Objects.equals(updateUser.getEmail(), userFromDataBase.getEmail())) throw new UserAlreadyExistsException();
        if (userFromDataBase.getDeletedAt().isPresent()) throw new UserNotFoundException();

        userFromDataBase.setName(updateUser.getName());
        userFromDataBase.setEmail(updateUser.getEmail());
        userFromDataBase.setPassword(updateUser.getPassword());
        userFromDataBase.setRole(updateUser.getRole());

        userRepository.save(userFromDataBase);
    }

    /**
     * Busca usuário na base de dados
     * @param username ID do usuário
     * @return Usuário encontrado
     * @throws UserNotFoundException Se não encontrar usuário
     */
    public User getUser(String username) throws UserNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(UserNotFoundException::new);

        if (user.getDeletedAt().isPresent()) throw new UserNotFoundException();

        return user;
    }

    /**
     * Deleta usuário
     * @param user Usuário
     */
    public void deleteUser(User user) throws UserNotFoundException {
        user.setDeletedAt(LocalDateTime.from(LocalDateTime.now()));

        userRepository.save(user);
    }

    /**
     * Verifica se usuário já existe na base de dados. Valida campos chave
     * @param user Usuário
     * @return Se usuário já existe
     */
    private boolean userExists(User user) {
        return userRepository.existsById(user.getId()) ||
                userRepository.existsByEmail(user.getEmail());
    }

}
