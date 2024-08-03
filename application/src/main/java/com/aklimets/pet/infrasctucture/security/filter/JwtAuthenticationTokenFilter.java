package com.aklimets.pet.infrasctucture.security.filter;

import com.aklimets.pet.application.util.jwt.JwtExtractor;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.jwt.JwtUser;
import com.aklimets.pet.domain.exception.NotFoundException;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.infrasctucture.security.handler.JwtSuccessHandler;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
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

import static java.lang.String.format;


@Slf4j
public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    public String authorizationHeader;

    public String accessPrefix;

    private final JwtExtractor jwtExtractor;

    private final UserRepository userRepository;


    public JwtAuthenticationTokenFilter(String authorizationHeader,
                                        String accessPrefix,
                                        AuthenticationManager authenticationManager,
                                        JwtSuccessHandler jwtSuccessHandler,
                                        JwtExtractor jwtExtractor,
                                        UserRepository userRepository) {
        super("/api/v1/common/user/**");
        setRequiresAuthenticationRequestMatcher(new OrRequestMatcher(
                // all urls which should be authenticated should be listed here, because if not then the auth context
                // will be null and spring security will return 401 because of missing authentication
                new AntPathRequestMatcher("/api/v1/common/user/**"),
                new AntPathRequestMatcher("/api/v1/common/details/**"))
        );
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(jwtSuccessHandler);
        this.jwtExtractor = jwtExtractor;
        this.userRepository = userRepository;
        this.authorizationHeader = authorizationHeader;
        this.accessPrefix = accessPrefix;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        var accessToken = extractTokenFromHeader(request);

        if (isAccessTokenInvalid(accessToken)) {
            sendUnauthorizedError(response);
            return null;
        }
        Authentication authentication = null;
        try {
            authentication = createUserAuthentication(jwtExtractor.extractAccessJwtUser(extractTokenValue(accessToken)));
        } catch (JwtException e) {
            log.warn("Error during jwt verification: {}", e.getMessage());
            sendUnauthorizedError(response);
        }
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }

    private String extractTokenValue(String accessToken) {
        return accessToken.substring(accessPrefix.length() + 1);
    }

    private void sendUnauthorizedError(HttpServletResponse response) throws IOException {
        response.sendError(401, "UNAUTHORIZED");
    }

    private boolean isAccessTokenInvalid(String accessToken) {
        return accessToken == null || !accessToken.startsWith(accessPrefix);
    }

    private UserAuthentication createUserAuthentication(JwtUser jwtUser) {
        var user = userRepository.findById(jwtUser.id())
                .orElseThrow(() -> new NotFoundException("Error not found", format("User with id %s not found", jwtUser.id())));
        return new UserAuthentication(user.getId(), user.getUsername(), user.getAuthorities());
    }
}
