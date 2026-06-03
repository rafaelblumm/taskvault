package com.taskvault.app.controller;

import com.taskvault.app.model.Task;
import com.taskvault.app.model.TaskStatus;
import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;
import com.taskvault.app.payload.request.CreateTaskRequest;
import com.taskvault.app.payload.response.TaskResponse;
import com.taskvault.app.repository.TaskRepository;
import com.taskvault.app.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Testes de integração dos endpoints de gestão de tarefas */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIT extends AuthenticatedControllerIT {

    /** Data/hora atual para uso em testes */
    private static final LocalDateTime NOW_DATE_TIME = LocalDateTime.now();

    /** Acesso aos dados de tarefas */
    @Autowired
    TaskRepository taskRepository;

    /** Acesso a camada de persistência de dados de usuários */
    @Autowired
    private UserRepository userRepository;

    /** Testa criação de tarefas */
    @Test
    public void createTaskTest() {
        var assignee = new User(
            "assignee",
            "assignee",
            "assignee@gmail.com",
            UserRole.ADMIN,
            "AssigneePassword"
        );
        userRepository.save(assignee);

        var task = new CreateTaskRequest(
                "Tarefa teste",
                Optional.of("Descrição!"),
                Optional.of(LocalDate.from(NOW_DATE_TIME)),
                Optional.of(assignee.getId())
        );
        var expectedResponse = new TaskResponse(
                0,
                task.title(),
                task.description(),
                TaskStatus.PENDING,
                NOW_DATE_TIME,
                task.dueDate(),
                "testuser",
                task.assignee()
        );

        final Long[] generatedId = new Long[1];
        getClient().post()
                .uri("/task")
                .headers((headers) -> headers.setBearerAuth(getAuthToken()))
                .accept(MediaType.APPLICATION_JSON)
                .body(task)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody(TaskResponse.class)
                .consumeWith((result) -> {
                    assertNotNull(result.getResponseBody());

                    generatedId[0] = result.getResponseBody().id();

                    TaskResponse response = result.getResponseBody();

                    assertEquals(expectedResponse.title(), response.title());
                    assertEquals(expectedResponse.description(), response.description());
                    assertEquals(expectedResponse.status(), response.status());
                    assertTrue(response.creationDatetime().isAfter(expectedResponse.creationDatetime()));
                    assertEquals(expectedResponse.dueDate(), response.dueDate());
                    assertEquals(expectedResponse.creator(), response.creator());
                    assertEquals(expectedResponse.assignee(), response.assignee());
                });

        Optional<Task> checkIfTaskWasSaved = taskRepository.findById(generatedId[0]);
        assertTrue(checkIfTaskWasSaved.isPresent());

        Task createdTask = checkIfTaskWasSaved.get();
        User creator = createdTask.getCreator();

        assertEquals(generatedId[0], createdTask.getId());
        assertEquals(expectedResponse.title(), createdTask.getTitle());
        assertEquals(expectedResponse.description(), createdTask.getDescription());
        assertEquals(expectedResponse.status(), createdTask.getStatus());
        assertTrue(createdTask.getCreationDatetime().isAfter(expectedResponse.creationDatetime()));
        assertEquals(expectedResponse.dueDate(), createdTask.getDueDate());
        assertEquals(expectedResponse.creator(), creator.getId());
        assertEquals(expectedResponse.assignee(), Optional.ofNullable(assignee.getId()));
    }

    /** Testa erro de usuário não encontrado na criação de tarefas */
    @Test
    public void userAssigneeNotFoundTest() {
        var task = new CreateTaskRequest(
                "Tarefa teste",
                Optional.of("Descrição!"),
                Optional.of(LocalDate.from(NOW_DATE_TIME)),
                Optional.of("UsuárioNãoExistente")
        );

        getClient().post()
                .uri("/task")
                .headers((headers) -> headers.setBearerAuth(getAuthToken()))
                .accept(MediaType.APPLICATION_JSON)
                .body(task)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }
}
