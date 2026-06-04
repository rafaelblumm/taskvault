package com.taskvault.app.repository;

import com.taskvault.app.model.TaskStatus;
import org.springframework.data.repository.CrudRepository;

import com.taskvault.app.model.Task;

import java.time.LocalDate;
import java.util.List;

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

}
