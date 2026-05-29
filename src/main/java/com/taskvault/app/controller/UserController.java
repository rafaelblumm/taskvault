package com.taskvault.app.controller;

import com.taskvault.app.payload.request.CreateUserRequest;
import com.taskvault.app.payload.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.taskvault.app.error.UserAlreadyExistsException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.payload.response.UserResponse;
import com.taskvault.app.service.UserService;

/** Controller do endpoint de gerenciamento de usuários */
@RestController
@RequestMapping("/user")
public class UserController {

    /** Serviço de gerenciamento de usuários */
    @Autowired
    private UserService userService;

    /**
     * Cria novo usuário
     * @param user Dados do usuário sendo criado
     * @return Usuário criado
     * @throws UserAlreadyExistsException Dados conflitantes com outro usuário
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody CreateUserRequest userDto) throws UserAlreadyExistsException {
        return UserResponse.from(userService.createUser(userDto));
    }

    /**
     * Envia informações de um usuário
     * @param username Nome de usuário procurado
     * @return Dados do usuário
     * @throws UserNotFoundException Usuário não encontrado
     */
    @GetMapping("/{username}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserResponse findUser(@PathVariable String username) throws UserNotFoundException {
        return UserResponse.from(userService.getUser(username));
    }

    /**
     * Atualiza informações de um usuário
     * @param username Nome de usuário a ser alterado
     * @param updateUser Dados alterados do usuário
     * @throws UserNotFoundException Usuário não encontrado
     * @throws UserAlreadyExistsException Dados alterados conflitam com outro usuário
     */
    @PutMapping("/{username}")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateUser(@PathVariable String username, @RequestBody UpdateUserRequest userDto)
    throws UserNotFoundException, UserAlreadyExistsException {
        userService.updateUser(username, userDto);
    }

    /**
     * Deleta usuário
     * @param username Nome de usuário a ser deletado
     * @throws UserNotFoundException Usuário não encontrado
     */
    @DeleteMapping("/{username}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username) throws UserNotFoundException {
        userService.deleteUser(username);
    }

}
