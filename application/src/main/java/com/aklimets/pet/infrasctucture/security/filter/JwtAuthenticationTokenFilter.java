package com.aklimets.pet.infrasctucture.security.filter;

import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.infrasctucture.security.handler.JwtSuccessHandler;
import com.aklimets.pet.jwt.model.JwtUser;
import com.aklimets.pet.jwt.util.JwtExtractor;
import com.aklimets.pet.model.attribute.AccessToken;
import com.aklimets.pet.model.attribute.Username;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Slf4j
public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTH_HEADER = "Authorization";

    private static final String ACCESS_PREFIX = "Bearer";

    private final JwtExtractor jwtExtractor;


    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager,
                                        JwtSuccessHandler jwtSuccessHandler,
                                        JwtExtractor jwtExtractor) {
        super("/api/v1/user/**");
        setRequiresAuthenticationRequestMatcher(new OrRequestMatcher(
                // all urls which should be authenticated should be listed here, because if not then the auth context
                // will be null and spring security will return 401 because of missing authentication
                new AntPathRequestMatcher("/api/v1/user/**"),
                new AntPathRequestMatcher("/api/v1/profile/**"))
        );
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(jwtSuccessHandler);
        this.jwtExtractor = jwtExtractor;
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
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    private AccessToken extractTokenValue(String accessToken) {
        return new AccessToken(accessToken.substring(ACCESS_PREFIX.length() + 1));
    }

    private void sendUnauthorizedError(HttpServletResponse response) throws IOException {
        response.sendError(401, "UNAUTHORIZED");
    }

    private boolean isAccessTokenInvalid(String accessToken) {
        return accessToken == null || !accessToken.startsWith(ACCESS_PREFIX);
    }

    private UserAuthentication createUserAuthentication(JwtUser jwtUser) {
        return new UserAuthentication(new UserIdNumber(jwtUser.id().getValue()),
                new Username(jwtUser.username().getValue()),
                List.of(new SimpleGrantedAuthority(jwtUser.role().name())));
    }
}
