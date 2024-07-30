package com.aklimets.pet.infrasctucture.swagger.impl;

import com.aklimets.pet.infrasctucture.security.annotation.WithBasicAuth;
import com.aklimets.pet.infrasctucture.swagger.AuthConfig;
import org.springframework.stereotype.Component;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.util.List;

@Component
@WithBasicAuth
public class BasicAuthConfigBean implements AuthConfig {
    @Override
    public List<SecurityScheme> getApiKeys() {
        return List.of(new BasicAuth("Basic auth"));
    }

    @Override
    public List<SecurityContext> getSecurityContext() {
        return List.of(SecurityContext.builder().securityReferences(defaultAuth()).build());
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = { authorizationScope };
        return List.of(new SecurityReference("Basic auth", authorizationScopes));
    }
}
