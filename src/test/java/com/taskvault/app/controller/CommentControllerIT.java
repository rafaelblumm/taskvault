package com.taskvault.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.taskvault.app.model.TaskStatus;
import com.taskvault.app.payload.request.CommentRequest;
import com.taskvault.app.payload.request.CreateTaskRequest;
import com.taskvault.app.payload.response.CommentResponse;
import com.taskvault.app.payload.response.TaskResponse;

/** Testes de integração dos endpoints de gerenciamento de comentários em tarefas */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CommentControllerIT extends AuthenticatedControllerIT {

    /** Testa criação de comentário em tarefas */
    @Test
    public void createCommentTest() {
        var taskRequest = new CreateTaskRequest(
            "Tarefa teste",
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        );
        getClient().post()
            .uri("/task")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .accept(MediaType.APPLICATION_JSON)
            .body(taskRequest)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CREATED)
            .expectBody(TaskResponse.class)
            .consumeWith((result) -> {
                TaskResponse response = result.getResponseBody();
                assertEquals(1, response.id());
                assertEquals("Tarefa teste", response.title());
                assertTrue(response.description().isEmpty());
                assertEquals(TaskStatus.PENDING, response.status());
                assertNotNull(response.creationDatetime());
                assertTrue(response.dueDate().isEmpty());
                assertEquals("testuser", response.creator());
                assertTrue(response.assignee().isEmpty());
            });

        var commentRequest = new CommentRequest("Comentário teste!");
        getClient().post()
            .uri("/task/1/comment")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .accept(MediaType.APPLICATION_JSON)
            .body(commentRequest)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CREATED)
            .expectBody(CommentResponse.class)
            .consumeWith((result) -> {
                CommentResponse response = result.getResponseBody();
                assertEquals(1, response.id());
                assertEquals(1, response.task());
                assertEquals("testuser", response.creator());
                assertEquals("Comentário teste!", response.message());
                assertNotNull(response.creationDatetime());
            });

        commentRequest = new CommentRequest("Comentário em tarefa inexistente");
        getClient().post()
            .uri("/task/9999999999/comment")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .accept(MediaType.APPLICATION_JSON)
            .body(commentRequest)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

}
