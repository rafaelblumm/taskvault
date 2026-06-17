package com.taskvault.app.repository;

import com.taskvault.app.model.TaskStatus;
import org.springframework.data.repository.CrudRepository;

import com.taskvault.app.model.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Camada de acesso aos dados da tabela de tarefas */
public interface TaskRepository extends CrudRepository<Task, Long> {

    /**
     * Busca todas as tarefas designadas a um usuário ordenados por data de criação crescente
     * @param userId ID do usuário
     * @return Lista de tarefas
     */
    public List<Task> findAllByAssigneeIdOrderByCreationDatetimeAsc(String userId);

    /**
     * Busca todas as tarefas que satisfazem as filtragens
     * @param assigneeId ID do usuário
     * @param status ID do usuário
     * @param dueDate ID do usuário
     * @return Lista de tarefas
     */
    List<Task> findAllByAssigneeIdAndStatusAndDueDateBeforeOrderByCreationDatetimeAsc(
            String assigneeId,
            TaskStatus status,
            LocalDate dueDate
    );

        @Query("SELECT t FROM Task t WHERE "
            + "(:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%',:title,'%'))) AND "
            + "(:status IS NULL OR t.status = :status) AND "
            + "(:createdBy IS NULL OR t.creator.id = :createdBy) AND "
            + "(:assignedTo IS NULL OR t.assignee.id = :assignedTo) AND "
            + "(:createdBefore IS NULL OR t.creationDatetime < :createdBefore) AND "
            + "(:createdAfter IS NULL OR t.creationDatetime > :createdAfter) AND "
            + "(:dueBefore IS NULL OR t.dueDate < :dueBefore) AND "
            + "(:dueAfter IS NULL OR t.dueDate > :dueAfter) "
            + "ORDER BY t.creationDatetime ASC")
        List<Task> findByFilters(
            @Param("title") String title,
            @Param("status") TaskStatus status,
            @Param("createdBy") String createdBy,
            @Param("assignedTo") String assignedTo,
            @Param("createdBefore") LocalDateTime createdBefore,
            @Param("createdAfter") LocalDateTime createdAfter,
            @Param("dueBefore") LocalDate dueBefore,
            @Param("dueAfter") LocalDate dueAfter
        );

}
