package com.taskvault.app.model;

import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.annotations.SQLRestriction;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/** Comentário em tarefas */
@Entity
@SQLRestriction("deleted_at IS NULL")
public class Comment {

    /** Identificador único */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Tarefa comentada */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Task task;

    /** Usuário criador do comentário */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User creator;

    /** Mensagem do comentário */
    private String message;

    /** Data de criação do comentário */
    private LocalDateTime creationDatetime;

    /** Data de deleção do comentário */
    @Nullable
    private LocalDateTime deletedAt;

    /** Construtor padrão */
    public Comment() {}

    /**
     * Cria novo comentário
     * @param task Tarefa commentada
     * @param creator Criador do comentário
     * @param message Mensagem do comentário
     */
    public Comment(Task task, User creator, String message) {
        this.id = 0;
        this.task = task;
        this.creator = creator;
        this.message = message;
        this.creationDatetime = LocalDateTime.now();
        this.deletedAt = null;
    }

    /**
     * Busca ID
     * @return Identificador único
     */
    public long getId() {
        return this.id;
    }

    /**
     * Define ID
     * @param id Identificador único
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Busca tarefa associada ao comentário
     * @return Tarefa associada
     */
    public Task getTask() {
        return this.task;
    }

    /**
     * Define tarefa associada ao comentário
     * @param task Tarefa associada
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Busca usuário criador do comentário
     * @return Usuário criador
     */
    public User getCreator() {
        return this.creator;
    }

    /**
     * Define usuário criador do comentário
     * @param creator Usuário criador
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * Busca mensagem do comentário
     * @return Mensagem
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Define mensagem do comentário
     * @param message Mensagem
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Busca data/hora de criação do comentário
     * @return Data/hora de criação
     */
    public LocalDateTime getCreationDatetime() {
        return this.creationDatetime;
    }

    /**
     * Define data/hora de criação do comentário
     * @param creationDatetime Data/hora de criação
     */
    public void setCreationDatetime(LocalDateTime creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    /**
     * Busca data/hora de remoção do comentário, se houver
     * @return Data/hora de remoção
     */
    public Optional<LocalDateTime> getDeletedAt() {
        return Optional.ofNullable(this.deletedAt);
    }

    /**
     * Define data/hora de remoção do comentário
     * @param deletedAt Data/hora de remoção
     */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Comment)) return false;

        var o = (Comment) obj;
        return getId() == o.getId() &&
            getTask().equals(o.getTask()) &&
            getCreator().equals(o.getCreator()) &&
            getMessage().equals(o.getMessage()) &&
            getCreationDatetime().equals(o.getCreationDatetime()) &&
            getDeletedAt().equals(o.getDeletedAt());
    }

}
