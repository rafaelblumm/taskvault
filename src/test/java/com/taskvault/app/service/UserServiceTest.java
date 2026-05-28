package com.taskvault.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.taskvault.app.payload.request.UpdateUserRequest;
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

import java.time.LocalDateTime;
import java.util.Optional;

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

        String encodedPassword = user.getPassword() + "[encoded]";
        var expected = new User(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            encodedPassword
        );

        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(expected);
        var createdUser = userService.createUser(user);

        verify(userRepository, times(1)).save(ArgumentMatchers.any());

        assertEquals(expected, createdUser);
    }

    /** Testa atualização de usuário **/
    @Test
    public void updateUserTest() {
        var userFromDatabase = new User(
                "teste",
                "Usuário Teste",
                "teste@dev.com",
                UserRole.GUEST,
                "SuperSecretPassword123"
        );

        var updatedUser = new UpdateUserRequest(
                "testeAtualizado",
                "testeAtualizado@dev.com",
                "SenhaAtualizada",
                UserRole.GUEST
        );

        userService.updateUser(userFromDatabase, updatedUser);

        assertEquals(userFromDatabase.getName(), updatedUser.getName());
        assertEquals(userFromDatabase.getEmail(), updatedUser.getEmail());
        assertEquals(userFromDatabase.getPassword(), passwordEncoder.encode(updatedUser.getPassword()));
        assertEquals(userFromDatabase.getRole(), updatedUser.getRole());
    }

    /** Testa procura de usuário **/
    @Test
    public void getUserTest() {
        var user = new User(
                "teste",
                "Usuário Teste",
                "teste@dev.com",
                UserRole.GUEST,
                "SuperSecretPassword123"
        );

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertEquals(userService.getUser(user.getId()), user);
    }

    /** Testa deleção de usuário **/
    @Test
    public void deleteUserTest() {
        var user = new User(
                "teste",
                "Usuário Teste",
                "teste@dev.com",
                UserRole.GUEST,
                "SuperSecretPassword123"
        );

        // Evita erro de milissegundos comparando se a data é após o início do teste
        LocalDateTime antesDaDelecao = LocalDateTime.now().minusSeconds(1);
        userService.deleteUser(user);

        assertTrue(user.getDeletedAt().get().isAfter(antesDaDelecao));
    }
}
