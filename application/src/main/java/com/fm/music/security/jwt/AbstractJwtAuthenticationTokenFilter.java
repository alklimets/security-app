package com.fm.music.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractJwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    @Value("${jwt.access.token.header}")
    protected String accessHeader;

    @Value("${jwt.refresh.token.header}")
    protected String refreshHeader;

    protected final JwtValidator jwtValidator;


    public AbstractJwtAuthenticationTokenFilter(JwtValidator validator) {
        super("/api/**/common/author/**");
        this.jwtValidator = validator;
        setRequiresAuthenticationRequestMatcher(new OrRequestMatcher(
                new AntPathRequestMatcher("/api/**/common/album/**"),
                new AntPathRequestMatcher("/api/**/common/author/**"),
                new AntPathRequestMatcher("/api/**/common/genre/**"),
                new AntPathRequestMatcher("/api/**/common/profile/**"),
                new AntPathRequestMatcher("/api/**/common/song/**"))
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
