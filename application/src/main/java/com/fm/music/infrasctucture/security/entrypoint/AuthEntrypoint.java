package com.fm.music.infrasctucture.security.entrypoint;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns generic response if auth failed
 */
@Component
public class AuthEntrypoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(401);
        response.getOutputStream().print("{\"errorCode\":\"401\",\"message\":\"UNAUTHORIZED\"}");
    }

}
