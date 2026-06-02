package com.taskvault.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.CrudRepository;

import com.taskvault.app.model.Comment;

/** Acesso a persistência de comentários */
public interface CommentRepository extends CrudRepository<Comment, Long> {

    /**
     * Busca todos os comentários vinculados a uma tarefa ordenados por data de criação crescente
     * @param taskId ID da tarefa
     * @return Lista de comentários
     */
    public List<Comment> findAllByTaskIdOrderByCreationDatetimeAsc(long taskId);

    /**
     * Atualiza campo com data/hora de deleção de todos os comentários de uma tarefa
     * @param deletedAt Nova data/hora de deleção
     * @param taskId ID da tarefa
     */
    @Transactional
    @Modifying
    @Query("UPDATE Comment c SET c.deletedAt = ?1 WHERE c.task.id = ?2")
    public void updateDeletedAtByTaskId(LocalDateTime deletedAt, long taskId);
}
