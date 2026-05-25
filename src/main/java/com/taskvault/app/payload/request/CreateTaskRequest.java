package com.taskvault.app.payload.request;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Dados da requisição de criação de tarefas
 * @param title Título da tarefa
 * @param description Descrição da tarefa
 * @param dueDate Data de conclusão prevista
 * @param assignee ID do usuário designado para a tarefa
 */
public record CreateTaskRequest(
    String title,
    String description,
    Optional<LocalDate> dueDate,
    Optional<String> assignee
) {}
