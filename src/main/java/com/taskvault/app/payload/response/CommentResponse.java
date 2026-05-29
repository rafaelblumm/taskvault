package com.taskvault.app.payload.response;

import java.time.LocalDateTime;

import com.taskvault.app.model.Comment;

/**
 * Resposta de requisições de comentários
 * @param id Identificador do comentário
 * @param task Identificador da tarefa
 * @param creator Identificador do criador
 * @param message Mensagem do comentário
 * @param creationDatetime Data/hora de criação
 */
public record CommentResponse(
    long id,
    long task,
    String creator,
    String message,
    LocalDateTime creationDatetime
) {

    /**
     * Cria {@Code CommentResponse} a partir de {@Code Comment}
     * @param comment Comentário
     * @return Resposta da requisição
     */
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getTask().getId(),
            comment.getCreator().getId(),
            comment.getMessage(),
            comment.getCreationDatetime()
        );
    }

}
