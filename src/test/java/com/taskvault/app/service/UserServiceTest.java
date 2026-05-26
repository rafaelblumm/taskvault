package com.taskvault.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;
import com.taskvault.app.repository.UserRepository;

/** Testes da classe de gestão de usuários */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    /** Testa criação de novo usuário */
    @Test
    public void createUserTest() {
        var user = new User(
            "teste",
            "Usuário Teste",
            "teste@dev.com",
            UserRole.GUEST,
            "SuperSecretPassword123"
        );

        when(userRepository.save(any(User.class))).thenReturn(user);
        var createdUser = userService.createUser(user);

        verify(userRepository, times(1)).save(ArgumentMatchers.any());

        assertEquals(user, createdUser);
    }

}
