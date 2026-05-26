package com.taskvault.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.taskvault.app.payload.request.CreateTaskRequest;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/** Registro de tarefa */
@Entity
public class Task {

    /** Identificador único */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Título da tarefa */
    private String title;

    /** Descrição da tarefa */
    @Nullable
    private String description;

    /** Status da tarefa */
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    /** Data de criação */
    private LocalDateTime creationDatetime;

    /** Data prevista para conclusão */
    @Nullable
    private LocalDate dueDate;

    /** Usuário criador da tarefa */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User creator;

    /** ID do usuário designado para a tarefa */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    @Nullable
    private User assignee;

    /** Data de remoção da tarefa */
    @Nullable
    private LocalDateTime deletedAt;

    /** Construtor padrão */
    public Task() {}

    /**
     * Cria nova instância com dados padrões a partir de DTO
     * @param taskDto DTO de tarefa
     * @param creator Usuário criador
     */
    public Task(CreateTaskRequest taskDto, User creator, Optional<User> assignee) {
        this.id = 0;
        this.title = taskDto.title();
        this.description = taskDto.description().orElse(null);
        this.status = TaskStatus.PENDING;
        this.creationDatetime = LocalDateTime.now();
        this.dueDate = taskDto.dueDate().orElse(null);
        this.creator = creator;
        this.assignee = assignee.orElse(null);
        this.deletedAt = null;
    }

    /**
     * Busca ID da tarefa
     * @return ID
     */
    public long getId() {
        return this.id;
    }

    /**
     * Define ID da tarefa
     * @param id Novo ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Busca título da tarefa
     * @return Título da tarefa
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Define título da tarefa
     * @param title Novo título da tarefa
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Busca descrição da tarefa
     * @return Descrição da tarefa, se houver
     */
    public Optional<String> getDescription() {
        return Optional.ofNullable(this.description);
    }

    /**
     * Define descrição da tarefa
     * @return Nova descrição da tarefa
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Busca status de conclusão da tarefa
     * @return Status
     */
    public TaskStatus getStatus() {
        return this.status;
    }

    /**
     * Define status de conclusão da tarefa
     * @param status Novo status
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Busca data/hora de criação da tarefa
     * @return Data/hora de criação
     */
    public LocalDateTime getCreationDatetime() {
        return this.creationDatetime;
    }

    /**
     * Define data/hora de criação da tarefa
     * @param creationDatetime Nova data/hora de criação
     */
    public void setCreationDatetime(LocalDateTime creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    /**
     * Busca data de previsão para conclusão da tarefa
     * @return Data de conclusão, se houver
     */
    public Optional<LocalDate> getDueDate() {
        return Optional.ofNullable(this.dueDate);
    }

    /**
     * Define data de previsão para conclusão da tarefa
     * @param dueDate Nova data de conclusão
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Busca criador da tarefa
     * @return Usuário criador
     */
    public User getCreator() {
        return this.creator;
    }

    /**
     * Define usuário criador da tarefa
     * @param creator Novo usuário criador
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * Busca usuário designado para a tarefa
     * @return Usuário designado, se houver
     */
    public Optional<User> getAssignee() {
        return Optional.ofNullable(this.assignee);
    }

    /**
     * Define usuário designado para a tarefa
     * @param assignee Novo usuário designado
     */
    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    /**
     * Busca data/hora de remoção da tarefa
     * @return Data/hora de remoção, se houver
     */
    public Optional<LocalDateTime> getDeletedAt() {
        return Optional.ofNullable(this.deletedAt);
    }

    /**
     * Define data/hora de remoção da tarefa
     * @param deletedAt Nova data/hora de remoção da tarefa
     */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

}
