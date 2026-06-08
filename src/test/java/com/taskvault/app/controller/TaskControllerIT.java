package com.taskvault.app.controller;

import com.taskvault.app.model.Task;
import com.taskvault.app.model.TaskStatus;
import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;
import com.taskvault.app.payload.request.CreateTaskRequest;
import com.taskvault.app.payload.request.UpdateTaskRequest;
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

    /** Testa atualização de tarefas */
    @Test
    public void updateTaskTest() {
        long taskId = createSampleTask();
        Task task = taskRepository.findById(taskId).orElseThrow();

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(
                "Título novo",
                Optional.of("Descrição nova"),
                TaskStatus.IN_PROGRESS,
                Optional.of(LocalDate.from(NOW_DATE_TIME)),
                task.getAssignee().map(User::getId)
        );

        TaskResponse expectedResponse = new TaskResponse(
                taskId,
                updateTaskRequest.title(),
                updateTaskRequest.description(),
                updateTaskRequest.status(),
                task.getCreationDatetime(),
                updateTaskRequest.dueDate(),
                task.getCreator().getId(),
                updateTaskRequest.assignee()
        );

        getClient().put()
                .uri("/task/" + taskId)
                .headers((headers) -> headers.setBearerAuth(getAuthToken()))
                .accept(MediaType.APPLICATION_JSON)
                .body(updateTaskRequest)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(TaskResponse.class)
                .consumeWith((result) -> {
                    assertNotNull(result.getResponseBody());

                    TaskResponse response = result.getResponseBody();

                    assertEquals(expectedResponse.title(), response.title());
                    assertEquals(expectedResponse.description(), response.description());
                    assertEquals(expectedResponse.status(), response.status());
                    assertEquals(expectedResponse.creationDatetime(), response.creationDatetime());
                    assertEquals(expectedResponse.dueDate(), response.dueDate());
                    assertEquals(expectedResponse.creator(), response.creator());
                    assertEquals(expectedResponse.assignee(), response.assignee());
                });
    }

    /** Testa atualização de tarefa não existente */
    @Test
    public void updateNonExistentTaskTest() {
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(
                "Título novo",
                Optional.of("Descrição nova"),
                TaskStatus.IN_PROGRESS,
                Optional.of(LocalDate.from(NOW_DATE_TIME)),
                Optional.of("NonExistent")
        );

        getClient().put()
                .uri("/task/" + 999)
                .headers((headers) -> headers.setBearerAuth(getAuthToken()))
                .accept(MediaType.APPLICATION_JSON)
                .body(updateTaskRequest)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    /** Testa procura de tarefa */
    @Test
    public void getTaskTest() {
        long taskId = createSampleTask();
        Task task = taskRepository.findById(taskId).orElseThrow();

        TaskResponse expectedResponse = new TaskResponse(
                taskId,
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreationDatetime(),
                task.getDueDate(),
                task.getCreator().getId(),
                task.getAssignee().map(User::getId)
        );

        getClient().get()
                .uri("/task/" + taskId)
                .headers((headers) -> headers.setBearerAuth(getAuthToken()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(TaskResponse.class)
                .consumeWith((result) -> {
                    TaskResponse response = result.getResponseBody();

                    assertNotNull(response);
                    assertEquals(expectedResponse.title(), response.title());
                    assertEquals(expectedResponse.description(), response.description());
                    assertEquals(expectedResponse.status(), response.status());
                    assertEquals(expectedResponse.creationDatetime(), response.creationDatetime());
                    assertEquals(expectedResponse.dueDate(), response.dueDate());
                    assertEquals(expectedResponse.creator(), response.creator());
                    assertEquals(expectedResponse.assignee(), response.assignee());
                });
    }

    /** Testa procura de tarefa não existente */
    @Test
    public void getNonExistentTaskTest() {
        getClient().get()
                .uri("/task/" + 999)
                .headers((headers) -> headers.setBearerAuth(getAuthToken()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Cria tarefa para testes
     * @return ID da tarefa
     */
    public long createSampleTask() {
        var taskRequest = new CreateTaskRequest(
                "Tarefa teste",
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        return getClient().post()
                .uri("/task")
                .headers((headers) -> headers.setBearerAuth(getAuthToken()))
                .accept(MediaType.APPLICATION_JSON)
                .body(taskRequest)
                .exchange()
                .returnResult(TaskResponse.class)
                .getResponseBody()
                .id();
    }
}