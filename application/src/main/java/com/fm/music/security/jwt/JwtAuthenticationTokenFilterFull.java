package com.fm.music.security.jwt;

import com.fm.music.model.User;
import com.fm.music.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilterFull extends AbstractJwtAuthenticationTokenFilter {

    private final UserService userService;

    public JwtAuthenticationTokenFilterFull(UserService userService, JwtValidator validator) {
        super(validator);
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String accessToken = request.getHeader(accessHeader);
        String refreshToken = request.getHeader(refreshHeader);

        if (accessToken == null || refreshToken == null) {
            response.sendError(401);
            return null;
        }

        try {
            JwtUser refreshUser = jwtValidator.validateRefresh(refreshToken);
            User user = userService.loadUserByUsername(refreshUser.getUsername());
            if (!refreshToken.equals(user.getRefreshToken())) {
                throw new JwtException("Tokens are not equal");
            }
        } catch (JwtException e) {
            response.sendError(401);
            return null;
        }

        try {
            jwtValidator.validateAccess(accessToken);
        } catch (ExpiredJwtException e) {
            response.sendError(421);
            return null;
        }

        JwtAuthenticationToken token = new JwtAuthenticationToken(accessToken);
        return getAuthenticationManager().authenticate(token);
    }
}
