package com.taskvault.app.security.auth;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.taskvault.app.model.User;

/** Dados de autenticação de usuários */
public class UserDetailsImpl implements UserDetails {

    /** Dados do usuário */
    private User user;

    /**
     * Cria nova instância de {@Code UserDetailsImpl}
     * @param user Dados do usuário
     */
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    /**
     * Retorna usuário
     * @return Usuário
     */
    public User getUser() {
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId();
    }

    @Override
    public boolean isEnabled() {
        return user.getDeletedAt().isEmpty();
    }

}
