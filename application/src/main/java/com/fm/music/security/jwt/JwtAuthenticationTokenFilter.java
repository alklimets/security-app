package com.fm.music.security.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends AbstractJwtAuthenticationTokenFilter {

    public JwtAuthenticationTokenFilter(JwtValidator jwtValidator) {
        super(jwtValidator);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String accessToken = request.getHeader(accessHeader);

        if (accessToken == null) {
            response.sendError(401, "UNAUTHORIZED");
            return null;
        }
        try {
            jwtValidator.validateAccess(accessToken);
        } catch (JwtException e) {
            response.sendError(401, "UNAUTHORIZED");
            return null;
        }

        JwtAuthenticationToken token = new JwtAuthenticationToken(accessToken);
        return getAuthenticationManager().authenticate(token);
    }
}
