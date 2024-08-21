package com.aklimets.pet.infrasctucture.swagger.impl;

import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
import com.aklimets.pet.infrasctucture.swagger.AuthConfig;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.util.List;

@Component
@WithJwtAuth
public class JwtAuthConfigBean implements AuthConfig {

    private static final String ACCESS = "Authorization";
    private static final String OPTIONAL = "Optional";

    @Override
    public List<SecurityScheme> getApiKeys() {
        return List.of(
                new ApiKey(ACCESS, ACCESS, "header"),
                new ApiKey(OPTIONAL, OPTIONAL, OPTIONAL)
        );
    }

    @Override
    public List<SecurityContext> getSecurityContext() {
        return List.of(SecurityContext.builder().securityReferences(defaultAuth()).build());
    }

    private List<SecurityReference> defaultAuth() {
        var authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = { authorizationScope };
        return List.of(new SecurityReference(ACCESS, authorizationScopes));
    }
}
