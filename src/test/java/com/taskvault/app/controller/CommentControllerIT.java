package com.taskvault.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
        long taskId = createSampleTask();

        var commentRequest = new CommentRequest("Comentário teste!");
        getClient().post()
            .uri("/task/" + taskId + "/comment")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .accept(MediaType.APPLICATION_JSON)
            .body(commentRequest)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CREATED)
            .expectBody(CommentResponse.class)
            .consumeWith((result) -> {
                CommentResponse response = result.getResponseBody();
                assertEquals(taskId, response.task());
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

    /** Testa listagem de comentários em uma tarefa */
    @Test
    public void listCommentsTest() {
        long taskId = createSampleTask();
        List<CommentRequest> comments = List.of(
            "Primeiro comentário",
            "Segundo comentário",
            "Terceiro comentário",
            "Último comentário"
        ).stream().map(CommentRequest::new).toList();

        comments.forEach((request) ->
            getClient().post()
                .uri("/task/" + taskId + "/comment")
                .headers((headers) -> headers.setBearerAuth(getAuthToken()))
                .accept(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody(CommentResponse.class)
                .consumeWith((result) -> {
                    CommentResponse response = result.getResponseBody();
                    assertEquals(taskId, response.task());
                    assertEquals("testuser", response.creator());
                    assertEquals(request.message(), response.message());
                    assertNotNull(response.creationDatetime());
                })
        );

        getClient().get()
            .uri("/task/" + taskId + "/comment")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectBody(new ParameterizedTypeReference<List<CommentResponse>>() {})
            .consumeWith((result) -> {
                List<CommentResponse> response = result.getResponseBody();
                int idx = 0;
                for (CommentResponse r: response) {
                    assertEquals(comments.get(idx++).message(), r.message());
                    assertEquals(taskId, r.task());
                    assertEquals("testuser", r.creator());
                    assertNotNull(r.creationDatetime());
                }
            });
    }

    /** Testa listagem de comentários em tarefa sem comentário */
    @Test
    public void listCommentsEmptyTest() {
        long taskId = createSampleTask();
        getClient().get()
            .uri("/task/" + taskId + "/comment")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectBody(new ParameterizedTypeReference<List<CommentResponse>>() {})
            .consumeWith((result) -> {
                List<CommentResponse> response = result.getResponseBody();
                assertTrue(response.isEmpty());
            });
    }

    /** Testa listagem de comentários em tarefa inexistente */
    @Test
    public void listCommentsInvalidTaskTest() {
        getClient().get()
            .uri("/task/99999999/comment")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
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
