package com.taskvault.app.model;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class User {

    /** Identificador único de usuário */
    @Id
    private String id;
    /** Nome do usuário */
    private String name;
    /** Endereço de e-mail do usuário */
    private String email;
    /** Nível de permissões do usuário */
    @Enumerated(EnumType.STRING)
    private UserRole role;
    /** Senha do usuário */
    private String password;
    /** Data e hora de remoção do usuário */
    @Nullable
    private LocalDateTime deletedAt;

    /** Construtor padrão */
    public User() {}

    /**
     * Cria novo usuário
     * @param id ID de usuário
     * @param name Nome
     * @param email Endereço de e-mail
     * @param role Nível de permissão
     * @param password Senha
     */
    public User(String id, String name, String email, UserRole role, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
        this.deletedAt = null;
    }

    /**
     * Busca ID do usuário
     * @return Username
     */
    public String getId() {
        return this.id;
    }

    /**
     * Define ID do usuário
     * @param id Username
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Busca nome do usuário
     * @return Nome
     */
    public String getName() {
        return this.name;
    }

    /**
     * Define nome do usuário
     * @param name Nome
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Busca endereço de e-mail do usuário
     * @return Endereço de e-mail
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Define endereço de e-mail do usuário
     * @param Endereço de e-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Busca nível de permissão do usuário
     * @return Nível de permissão
     */
    public UserRole getRole() {
        return this.role;
    }

    /**
     * Define nível de permissão do usuário
     * @param Nível de permissão
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Busca senha do usuário
     * @return Senha do usuário
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Define senha do usuário
     * @param Senha do usuário
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Busca data e hora de remoção do usuário
     * @return Se for usuário ativo, retorna {@Code Optional.empty}. Se for
     * inativo, retorna data e hora de remoção
     */
    public Optional<LocalDateTime> getDeletedAt() {
        return Optional.ofNullable(this.deletedAt);
    }

    /**
     * Define data e hora de remoção do usuário
     * @param deletedAt Data e hora de remoção
     */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;

        var o = (User) obj;
        return getId().equals(o.getId()) &&
            getName().equals(o.getName()) &&
            getEmail().equals(o.getEmail()) &&
            getRole().equals(o.getRole()) &&
            getPassword().equals(o.getPassword()) &&
            getDeletedAt().equals(o.getDeletedAt());
    }

    @Override
    public String toString() {
        var deletionStr = getDeletedAt()
            .map((datetime) -> datetime.toString())
            .orElse("");
        var sb = new StringBuilder();
        sb.append("Id.......: ").append(getId())
            .append("\nName.....: ").append(getName())
            .append("\nEmail....: ").append(getEmail())
            .append("\nRole.....: ").append(getRole())
            .append("\nPassword.: ").append(getPassword())
            .append("\nDeletedAt: ").append(deletionStr);

        return sb.toString();
    }

}