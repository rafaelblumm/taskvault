package com.taskvault.app.controller;

import java.util.List;

import com.taskvault.app.error.CommentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.taskvault.app.error.TaskNotFoundException;
import com.taskvault.app.payload.request.CommentRequest;
import com.taskvault.app.payload.response.CommentResponse;
import com.taskvault.app.service.CommentService;

/** Controller do endpoint de gerenciamento de comentários */
@RestController
@RequestMapping("/task/{taskId}/comment")
public class CommentController {

    /** Serviço de gerenciamento de comentários */
    @Autowired
    private CommentService commentService;

    /**
     * Cria novo comentário em tarefa
     * @param taskId ID da tarefa comentada
     * @param comment Dados da requisição de criação de comentário
     * @return Comentário criado
     * @throws TaskNotFoundException
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommentResponse createComment(
        @PathVariable long taskId,
        @RequestBody CommentRequest comment
    ) throws TaskNotFoundException {
        return CommentResponse.from(commentService.createComment(comment, taskId));
    }

    /**
     * Lista comentários em uma tarefa
     * @param taskId ID da tarefa
     * @return Lista de comentários
     * @throws TaskNotFoundException Se tarefa não existir
     */
    @GetMapping
    @PreAuthorize("hasRole('GUEST')")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CommentResponse> listComments(@PathVariable long taskId) throws TaskNotFoundException {
        return commentService.listComments(taskId)
            .stream()
            .map(CommentResponse::from)
            .toList();
    }

    /**
     * Deleta comentário
     * @param commentId ID do comentário
     * @throws CommentNotFoundException Se o comentário não existir
     */
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long commentId) throws CommentNotFoundException {
        commentService.deleteComment(commentId);
    }

}
