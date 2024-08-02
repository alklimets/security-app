package com.aklimets.pet.infrasctucture.security.filter;

import com.aklimets.pet.application.util.jwt.JwtExtractor;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.jwt.JwtUser;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.infrasctucture.security.handler.JwtSuccessHandler;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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


public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Value("${security.authorization.header}")
    public String authorizationHeader;

    @Value("${jwt.access.token.prefix}")
    public String accessPrefix;
    private final JwtExtractor jwtExtractor;
    private final UserRepository userRepository;


    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager,
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
            LOGGER.warn("Error during jwt verification: {}", e.getMessage());
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

    private static void sendUnauthorizedError(HttpServletResponse response) throws IOException {
        response.sendError(401, "UNAUTHORIZED");
    }

    private boolean isAccessTokenInvalid(String accessToken) {
        return accessToken == null || !accessToken.startsWith(accessPrefix);
    }

    private UserAuthentication createUserAuthentication(JwtUser jwtUser) {
        var user = userRepository.findUserByUsername(jwtUser.username());
        return new UserAuthentication(user.getId(), user.getUsername(), user.getAuthorities());
    }
}
