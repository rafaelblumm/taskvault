package com.taskvault.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.taskvault.app.security.auth.UserDetailsImpl;
import com.taskvault.app.security.service.JWTService;

@Service
public class AuthService {

    /** Gerenciador de requisições de autenticação */
    @Autowired
    private AuthenticationManager authManager;

    /** Serviço de geração e validação de JWTs */
    @Autowired
    private JWTService jwtService;

    /**
     * Autentica usuário na aplicação e gera JWT
     * @param username Nome de usuário
     * @param password Senha do usuário
     * @return JWT
     */
    public String authenticateUser(String username, String password) {
        var userPassAuthToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authManager.authenticate(userPassAuthToken);
        var userDetails = (UserDetailsImpl) auth.getPrincipal();

        return jwtService.generateToken(userDetails);
    }

}
