package com.taskvault.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Filtros de busca de tarefas
 * @param titleContains Texto contido no título da tarefa
 * @param status Status da tarefa
 * @param creator ID do usuário criador da tarefa
 * @param assignee ID do usuário designado à tarefa
 * @param createdBefore Data/hora de criação anterior a esta
 * @param createdAfter Data/hora de criação posterior a esta
 * @param dueDateBefore Data prevista de conclusão anterior a esta
 * @param dueDateAfter Data prevista de conclusão posterior a esta
 */
public record TaskFilters(
    Optional<String> titleContains,
    Optional<TaskStatus> status,
    Optional<String> creator,
    Optional<String> assignee,
    Optional<LocalDateTime> createdBefore,
    Optional<LocalDateTime> createdAfter,
    Optional<LocalDate> dueDateBefore,
    Optional<LocalDate> dueDateAfter
) {}
