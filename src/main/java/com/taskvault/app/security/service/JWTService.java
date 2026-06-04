package com.taskvault.app.security.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.taskvault.app.security.auth.UserDetailsImpl;

/** Serviço de validação e emissão de JWTs */
@Service
public class JWTService {

    /** Chave para geração de tokens */
    @Value("${app.jwt.key}")
    private String key;

    /** Issuer para geração de tokens */
    @Value("${app.jwt.issuer}")
    private String issuer;

    /** Issuer para geração de tokens */
    @Value("${app.jwt.expiration-seconds}")
    private int expirationSeconds;

    /**
     * Cria novo token JWT
     * @param user Dados do usuário
     * @return Token JWT
     * @throws JWTCreationException Caso ocorra algum erro na criação do token
     */
    public String generateToken(UserDetailsImpl user) {
        return JWT.create()
            .withIssuer(issuer)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(expirationSeconds))
            .withSubject(user.getUsername())
            .sign(getAlgorithm());
    }

    /**
     * Recupera nome do usuário a partir do token
     * @param token Token JWT
     * @return Nome de usuário
     * @throws JWTVerificationException Se token for inválido
     */
    public String getUsername(String token) {
        return decodeToken(token).getSubject();
    }

    /**
     * Recupera data de expiração do token
     * @param token Token JWT
     * @return Data de expiração, se houver
     * @throws JWTVerificationException Se token for inválido
     */
    public Optional<Instant> getExpiration(String token) {
        return Optional.of(decodeToken(token).getExpiresAtAsInstant());
    }

    /**
     * Decodifica token
     * @param token JWT
     * @return Token decodificado
     * @throws JWTVerificationException Se token for inválido
     */
    private DecodedJWT decodeToken(String token) throws JWTVerificationException {
        return JWT.require(getAlgorithm())
            .withIssuer(issuer)
            .build()
            .verify(token);
    }

    /**
     * Cria algoritmo utilizado para assinar e validar JWT
     * @return Algoritmo
     */
    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(key);
    }

}
