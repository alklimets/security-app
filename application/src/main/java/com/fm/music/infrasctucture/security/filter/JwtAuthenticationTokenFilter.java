package com.fm.music.infrasctucture.security.filter;

import com.fm.music.application.util.jwt.JwtValidator;
import com.fm.music.domain.dto.jwt.JwtUser;
import com.fm.music.domain.model.user.User;
import com.fm.music.domain.model.user.UserRepository;
import com.fm.music.infrasctucture.security.handler.JwtSuccessHandler;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    @Value("${security.authorization.header}")
    public String authorizationHeader;

    @Value("${jwt.access.token.prefix}")
    public String accessPrefix;
    private final JwtValidator jwtValidator;
    private final UserRepository userRepository;


    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager,
                                        JwtSuccessHandler jwtSuccessHandler,
                                        JwtValidator jwtValidator,
                                        UserRepository userRepository) {
        super("/api/v1/common/security/**");
        setRequiresAuthenticationRequestMatcher(new OrRequestMatcher(
                new AntPathRequestMatcher("/api/v1/common/security/**"),
                new AntPathRequestMatcher("/api/v1/common/details/**"))
        );
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(jwtSuccessHandler);
        this.jwtValidator = jwtValidator;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        String accessToken = request.getHeader(authorizationHeader);

        if (accessToken == null || !accessToken.startsWith(accessPrefix)) {
            response.sendError(401, "UNAUTHORIZED");
            return null;
        }
        try {
            JwtUser jwtUser = jwtValidator.validateAccess(accessToken.substring(accessPrefix.length() + 1));
            User user = userRepository.findUserByUsername(jwtUser.username());
            return UsernamePasswordAuthenticationToken.authenticated(user.getId(), user.getUsername(), user.getAuthorities().stream().toList());
        } catch (JwtException e) {
            response.sendError(401, "UNAUTHORIZED");
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
