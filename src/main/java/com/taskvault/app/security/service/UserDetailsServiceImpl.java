package com.taskvault.app.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taskvault.app.repository.UserRepository;
import com.taskvault.app.security.auth.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /** Acesso a camada de dados de usuários */
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
            .map(UserDetailsImpl::new)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

}
