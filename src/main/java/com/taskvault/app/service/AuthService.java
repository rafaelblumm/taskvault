package com.taskvault.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.taskvault.app.security.auth.UserDetailsImpl;
import com.taskvault.app.security.service.JWTService;
import com.taskvault.app.security.service.UserDetailsServiceImpl;

/** Serviço de autenticação de usuários */
@Service
public class AuthService {

    /** Gerenciador de requisições de autenticação */
    @Autowired
    private AuthenticationManager authManager;

    /** Serviço de geração e validação de JWTs */
    @Autowired
    private JWTService jwtService;

    /** Serviço de busca de dados de usuários autenticados */
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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

    /**
     * Busca dados do usuário autenticado na aplicação
     * @return Usuário autenticado, se encontrado
     */
    public Optional<UserDetailsImpl> getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map((auth) -> (UserDetailsImpl) userDetailsService.loadUserByUsername(auth.getName()));
    }

}
