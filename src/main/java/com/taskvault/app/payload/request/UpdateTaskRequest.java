package com.taskvault.app.payload.request;

import java.time.LocalDate;
import java.util.Optional;

import com.taskvault.app.model.TaskStatus;

/**
 * Dados da atualização de tarefas
 * @param title Título da tarefa
 * @param description Descrição da tarefa
 * @param status Status da tarefa
 * @param dueDate Data de conclusão prevista
 * @param assignee ID do usuário designado para a tarefa
 */
public record UpdateTaskRequest(
    String title,
    Optional<String> description,
    TaskStatus status,
    Optional<LocalDate> dueDate,
    Optional<String> assignee
) {}
