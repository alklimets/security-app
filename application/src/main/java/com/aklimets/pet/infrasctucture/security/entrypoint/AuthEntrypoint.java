package com.aklimets.pet.infrasctucture.security.entrypoint;

import com.aklimets.pet.infrasctucture.security.handler.JwtSuccessHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns generic response if authentication failed
 */
@Component
public class AuthEntrypoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LogManager.getLogger(AuthEntrypoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        LOGGER.warn("Authentication failed: {}", authException.getMessage());
        response.setContentType("application/json");
        response.setStatus(401);
        response.getOutputStream().print("{\"errorCode\":\"401\",\"message\":\"UNAUTHORIZED\"}");
    }

}
