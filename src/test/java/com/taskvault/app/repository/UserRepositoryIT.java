package com.taskvault.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;

/** Teste de integração da camada de persistência de usuários */
@DataJpaTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    /** Teste de criação e atualização de usuários */
    @Test
    public void saveUserTest() {
        var user = new User(
            "johndoe",
            "John Doe",
            "johndoe@enterprise.com",
            UserRole.USER,
            "My!P4ssw0rd"
        );

        userRepository.save(user);
        Optional<User> createdUser = userRepository.findById(user.getId());
        assertTrue(createdUser.isPresent());
        assertEquals(user, createdUser.get());

        user.setName("New name");
        userRepository.save(user);
        Optional<User> updatedUser = userRepository.findById(user.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals(user, updatedUser.get());
    }

    /** Teste de validação de existência de usuário pelo e-mail */
    @Test
    public void existsByEmailTest() {
        var user = new User(
            "johndoe",
            "John Doe",
            "johndoe@enterprise.com",
            UserRole.USER,
            "My!P4ssw0rd"
        );
        userRepository.save(user);
        assertTrue(userRepository.existsByEmail(user.getEmail()));
    }

}
