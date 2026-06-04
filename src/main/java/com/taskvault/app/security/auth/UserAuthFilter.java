package com.taskvault.app.security.auth;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.taskvault.app.error.InvalidTokenException;
import com.taskvault.app.error.MissingAuthTokenException;
import com.taskvault.app.error.UnauthorizedException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.repository.UserRepository;
import com.taskvault.app.security.SecurityConfig;
import com.taskvault.app.security.SecurityUtils;
import com.taskvault.app.security.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Filtro validador de tokens de usuário */
@Component
public class UserAuthFilter extends OncePerRequestFilter {

    /** Nome do header de autorização contendo token de acesso */
    private static final String AUTH_HEADER_NAME = "Authorization";

    /** Serviço de emissão de validação de JWTs */
    @Autowired
    private JWTService jwtService;

    /** Acesso a camada de dados de usuários */
    @Autowired
    private UserRepository userRepository;

    /** Tokens revogados */
    @Autowired
    private RevokedTokensStore revokedTokensStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
        if (isAuthRequired(request)) {
            try {
                setAuthContext(getUserDetailsFromRequest(request));
            } catch (UnauthorizedException | InvalidTokenException | UserNotFoundException | MissingAuthTokenException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Indica se endpoint acessado pelo usuário necessita de autenticação
     * @param request Dados da requisição
     * @return Se é necessário autenticação
     */
    private boolean isAuthRequired(HttpServletRequest request) {
        return !SecurityConfig.AUTH_NOT_REQUIRED.contains(request.getRequestURI());
    }

    /**
     * Busca dados do usuário a partir do token informado na requisição
     * @param request Dados da requisição
     * @return Dados do usuário informado no token
     * @throws MissingAuthTokenException Se não for informado um token de autenticação
     * @throws UserNotFoundException Se usuário do token não for encontrado
     * @throws InvalidTokenException Se token informado for inválido
     */
    private UserDetailsImpl getUserDetailsFromRequest(HttpServletRequest request)
    throws MissingAuthTokenException, UserNotFoundException, InvalidTokenException {
        String token = getUserToken(request).orElseThrow(MissingAuthTokenException::new);
        if (revokedTokensStore.isRevoked(token))
            throw new UnauthorizedException();

        String username;
        try {
            username = jwtService.getUsername(token);
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException();
        }

        UserDetailsImpl userDetails = userRepository.findById(username)
            .map(UserDetailsImpl::new)
            .orElseThrow(UserNotFoundException::new);

        return userDetails;
    }

    /**
     * Busca token de acesso do usuário nos headers da requisição
     * @param request Dados da requisição
     * @return Se houver token, retorna encapsulado em {@Code Optional}.
     * Se não houver, retorna {@Code Optional.empty}
     */
    private Optional<String> getUserToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTH_HEADER_NAME))
            .map(SecurityUtils::stripBearerPrefix);
    }

    /**
     * Define contexto de autenticação a partir do usuário informado
     * @param userDetails Dados do usuário
     */
    private void setAuthContext(UserDetailsImpl userDetails) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            userDetails.getUsername(),
            null,
            userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
