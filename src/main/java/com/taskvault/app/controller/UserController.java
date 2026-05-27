package com.taskvault.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.taskvault.app.model.User;
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
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody User user) {
        return UserResponse.from(userService.createUser(user));
    }

    /**
     * Envia informações de um usuário
     * @param username Nome de usuário procurado
     * @return Dados do usuário
     */
    @GetMapping("/{username}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserResponse findUser(@PathVariable ("username") String username) { return UserResponse.from(userService.getUser(username)); }


}
