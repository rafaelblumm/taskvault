package com.taskvault.app.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.taskvault.app.error.InvalidTokenException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.model.Jwt;
import com.taskvault.app.model.User;
import com.taskvault.app.security.SecurityUtils;
import com.taskvault.app.security.auth.RevokedTokensStore;
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

    /** Tokens revogados */
    @Autowired
    private RevokedTokensStore revokedTokensStore;

    /**
     * Autentica usuário na aplicação e gera JWT
     * @param username Nome de usuário
     * @param password Senha do usuário
     * @return JWT
     */
    public Jwt authenticateUser(String username, String password) {
        var userPassAuthToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authManager.authenticate(userPassAuthToken);
        var userDetails = (UserDetailsImpl) auth.getPrincipal();

        return generateToken(userDetails);
    }

    /**
     * Desautentica usuário da aplicação
     * @param token Token do usuário
     * @throws InvalidTokenException Se token for inválido
     */
    public void deauthenticateCurrentUser(String bearerToken) throws InvalidTokenException {
        try {
            String token = SecurityUtils.stripBearerPrefix(bearerToken);
            Instant expirationTime = jwtService.getExpiration(token)
                .orElseThrow(InvalidTokenException::new);
            revokedTokensStore.add(token, expirationTime);
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException();
        }
    }

    /**
     * Atualiza token de usuário
     * @param bearerToken Token atual
     * @return Novo token
     * @throws UserNotFoundException Se não encontrar usuário autenticado
     * @throws InvalidTokenException Se token for inválido
     */
    public Jwt refreshUserAuth(String bearerToken) throws UserNotFoundException, InvalidTokenException {
        Jwt token = generateToken(getCurrentUser().orElseThrow(UserNotFoundException::new));
        deauthenticateCurrentUser(bearerToken);

        return token;
    }

    /**
     * Busca dados do usuário autenticado na aplicação
     * @return Usuário autenticado, se encontrado
     */
    public Optional<UserDetailsImpl> getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map((auth) -> (UserDetailsImpl) userDetailsService.loadUserByUsername(auth.getName()));
    }

    /**
     * Indica se usuário possui permissões para alterar um recurso criado por determinado usuário
     * @param resourceCreator Criador do recurso
     * @return Se possui permissão
     */
    public boolean canUpdateResource(User resourceCreator) {
        UserDetailsImpl user = getCurrentUser()
            .orElseThrow(UserNotFoundException::new);

        return user.isElevated() || resourceCreator.getId().equals(user.getUsername());
    }

    /**
     * Cria JWT para usuário autenticado
     * @param user Dados do usuário
     * @return Dados do token
     */
    private Jwt generateToken(UserDetailsImpl user) {
        String token = jwtService.generateToken(user);
        LocalDateTime expirationDateTime = jwtService.getExpiration(token)
            .map((exp) -> LocalDateTime.ofInstant(exp, ZoneId.systemDefault()))
            .orElseThrow(() -> new RuntimeException("Não foi possível buscar a data de expiração do token"));

        return new Jwt(token, expirationDateTime);
    }

}
