package com.taskvault.app.payload.request;

/**
 * Dados de requisições de criação de comentários em tarefas
 * @param message Mensagem do comentário
 */
public record CommentRequest(String message) {}
