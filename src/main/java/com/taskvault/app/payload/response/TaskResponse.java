package com.taskvault.app.payload.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.taskvault.app.model.Task;
import com.taskvault.app.model.TaskStatus;

/**
 * DTO de tarefa para resposta de requisições
 * @param id Identificador da tarefa
 * @param title Título da tarefa
 * @param description Descrição da tarefa
 * @param status Status de conclusão
 * @param creationDatetime Data de criação
 * @param dueDate Data de conclusão
 * @param creator ID do usuário criador
 * @param assignee ID do usuário designado para a tarefa
 */
public record TaskResponse(
    long id,
    String title,
    Optional<String> description,
    TaskStatus status,
    LocalDateTime creationDatetime,
    Optional<LocalDate> dueDate,
    String creator,
    Optional<String> assignee
) {

    /**
     * Cria DTO de resposta de tarefas a partir de uma instância de {@Code Task}
     * @param task Dados da tarefa
     * @return DTO de resposta
     */
    public static TaskResponse from(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getCreationDatetime(),
            task.getDueDate(),
            task.getCreator().getId(),
            task.getAssignee().map((user) -> user.getId())
        );
    }

}
