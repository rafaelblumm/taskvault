package com.taskvault.app.utils;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/** Classe utilitária para geração de hashes de senhas */
public class PasswordHasher {

    /** Tamanho do salt para hashing (em bytes) */
    private static int SALT_LENGTH = 32;
    /** Tamanho do hash gerado (em bytes) */
    private static int HASH_LENGTH = 72;
    /** Graus de paralelismo para geração do hash */
    private static int PARALLELISM = 1;
    /** Quantidade de memória para geração do hash (16MiB) */
    private static int MEMORY = 16384;
    /** Iterações na geração do hash */
    private static int ITERATIONS = 2;

    /**
     * Cria hash de senha de usuário
     * @param password Senha de usuário (plaintext)
     * @return Hash da senha
     */
    public static String hash(String password) {
        var encoder = new Argon2PasswordEncoder(
            SALT_LENGTH,
            HASH_LENGTH,
            PARALLELISM,
            MEMORY,
            ITERATIONS
        );
        return encoder.encode(password);
    }

}
