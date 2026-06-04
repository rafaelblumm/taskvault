package com.taskvault.app.security.auth;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** Armazenamento de tokens revogados com controle de limpeza de cache */
@Component
public class RevokedTokensStore {

    /** Mapa de tokens revogados e suas respectivas expirações */
    private final ConcurrentMap<String, Instant> tokens = new ConcurrentHashMap<>();

    /**
     * Adiciona token revogado
     * @param token Token
     * @param expirationTime Data/hora de expiração
     */
    public void add(String token, Instant expirationTime) {
        if (token == null || expirationTime == null) return;

        System.out.println("********************** token adicionado");
        tokens.put(token, expirationTime);
    }

    /**
     * Indica se token foi revogado
     * @param token Token
     * @return Se foi revogado
     */
    public boolean isRevoked(String token) {
        tokens.forEach((k, v) -> {
            System.out.println("****************** " + k + " = " + v.toString());
        });
        return tokens.containsKey(token);
    }

    /**
     * Tarefa de limpeza de tokens expirados
     */
    @Scheduled(fixedRateString = "${app.jwt.expiration-seconds}")
    public void cleanExpiredTokens() {
        var now = Instant.now();
        tokens.entrySet().removeIf(token -> now.isAfter(token.getValue()));
    }

}
