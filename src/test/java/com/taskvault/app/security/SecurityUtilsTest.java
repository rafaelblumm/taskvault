package com.taskvault.app.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Testes da classe utilitária de segurança */
public class SecurityUtilsTest {

    /** Testa processamento de token bearer */
    @Test
    public void stripBearerPrefixTest() {
        var result = SecurityUtils.stripBearerPrefix("Bearer first.second.third");
        assertEquals("first.second.third", result);

        var token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJUYXNrVmF1bHQtZGV2IiwiaWF0IjoxNzgwNTgxND" +
                    "AzLCJleHAiOjE3ODA1OTU4MDMsInN1YiI6Imd1ZXN0dXNlciJ9.mSUJRUYTAkQMspJ8LOYakUkC2ql0DQqnHKUhQw7p2JI";
        var expected = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJUYXNrVmF1bHQtZGV2IiwiaWF0IjoxNzgwNTgxNDAzLC" +
                       "JleHAiOjE3ODA1OTU4MDMsInN1YiI6Imd1ZXN0dXNlciJ9.mSUJRUYTAkQMspJ8LOYakUkC2ql0DQqnHKUhQw7p2JI";
        assertEquals(expected, SecurityUtils.stripBearerPrefix(token));
    }

}
