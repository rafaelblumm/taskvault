package com.taskvault.app.service;

import com.taskvault.app.payload.request.CreateUserRequest;
import com.taskvault.app.payload.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskvault.app.error.UserAlreadyExistsException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.model.User;
import com.taskvault.app.repository.UserRepository;

import java.time.LocalDateTime;

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
    public User createUser(CreateUserRequest userDto) throws UserAlreadyExistsException {
        if (userRepository.existsByIdOrEmail(userDto.id(), userDto.email()))
            throw new UserAlreadyExistsException();

        var user = new User(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Atualiza informações de um usuário
     * @param userFromDataBase Usuário a ser alterado
     * @param updateUser Dados alterados do usuário
     * @throws UserNotFoundException Usuário não encontrado
     * @throws UserAlreadyExistsException Dados alterados conflitam com outro usuário
     */
    public User updateUser(String username, UpdateUserRequest userDto)
    throws UserNotFoundException, UserAlreadyExistsException {
        User user = getUser(username);
        if (!user.getEmail().equals(userDto.email()) && userRepository.existsByEmail(userDto.email()))
            throw new UserAlreadyExistsException();

        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRole(userDto.role());

        return userRepository.save(user);
    }

    /**
     * Busca usuário na base de dados
     * @param username ID do usuário
     * @return Usuário encontrado
     * @throws UserNotFoundException Se não encontrar usuário
     */
    public User getUser(String username) throws UserNotFoundException {
        User user = userRepository.findById(username).orElseThrow(UserNotFoundException::new);

        if (user.getDeletedAt().isPresent()) throw new UserNotFoundException();

        return user;
    }

    /**
     * Deleta usuário
     * @param username ID do usuário a ser deletado
     * @throws UserNotFoundException Se usuário não existe
     */
    public void deleteUser(String username) throws UserNotFoundException {
        User user = getUser(username);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

}
