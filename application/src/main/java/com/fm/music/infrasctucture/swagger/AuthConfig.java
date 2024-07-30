package com.fm.music.infrasctucture.swagger;

import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.util.List;

public interface AuthConfig {
    List<SecurityScheme> getApiKeys();

    List<SecurityContext> getSecurityContext();
}
