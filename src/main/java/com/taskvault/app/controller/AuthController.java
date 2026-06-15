package com.taskvault.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskvault.app.payload.request.LoginRequest;
import com.taskvault.app.payload.response.LoginResponse;
import com.taskvault.app.service.AuthService;

/** Controller do endpoint de autenticação de usuários */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /** Serviço de autenticação de usuários */
    @Autowired
    private AuthService userAuthService;

    /**
     * Endpoint de autenticação de usuário
     * @param loginRequest Dados da requisição
     * @return Token de usuário e data de expiração
     */
    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        return userAuthService.authenticateUser(loginRequest.user(), loginRequest.pass());
    }

    /**
     * Endpoint de desautenticação de usuário
     * @param bearerToken Token de usuário
     */
    @PostMapping("/logout")
    public void deauthenticateUser(@RequestHeader("Authorization") String bearerToken) {
        userAuthService.deauthenticateCurrentUser(bearerToken);
    }

}
