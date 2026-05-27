package com.taskvault.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.taskvault.app.model.Task;

/** Camada de acesso aos dados da tabela de tarefas */
public interface TaskRepository extends CrudRepository<Task, Long> {}
