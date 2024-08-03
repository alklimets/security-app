package com.aklimets.pet.infrasctucture.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.aklimets.pet.application.envelope.ErrorResponseEnvelope;
import com.aklimets.pet.application.envelope.ResponseEnvelope;
import com.aklimets.pet.application.envelope.ValidationEnvelope;
import com.aklimets.pet.infrasctucture.security.annotation.NoAuth;
import com.aklimets.pet.infrasctucture.security.annotation.WithBasicAuth;
import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Config creates API definitions
 */
@Configuration
@Profile("swagger-enabled")
public class SwaggerConfig {

    @Autowired
    private TypeResolver typeResolver;

    @Autowired(required = false)
    private AuthConfig authConfig;

    @Bean
    public Docket securityApiWithoutAuth() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Security API")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v1/common/security/**"))
                .build()
                .additionalModels(typeResolver.resolve(ErrorResponseEnvelope.class),
                        typeResolver.resolve(ResponseEnvelope.class),
                        typeResolver.resolve(ValidationEnvelope.class));
    }

    @Bean
    @NoAuth
    public Docket profileApiWithoutAuth() {
        return getProfileApiDocket();
    }

    @Bean
    @WithBasicAuth
    @WithJwtAuth
    public Docket profileApi() {
        return getProfileApiDocket().securitySchemes(authConfig.getApiKeys()).securityContexts(authConfig.getSecurityContext());
    }

    private Docket getProfileApiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("User Details API")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v1/common/details/**"))
                .build()
                .additionalModels(typeResolver.resolve(ErrorResponseEnvelope.class),
                        typeResolver.resolve(ResponseEnvelope.class),
                        typeResolver.resolve(ValidationEnvelope.class));
    }

    @Bean
    @WithBasicAuth
    @WithJwtAuth
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("User API")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v1/common/user/**"))
                .build()
                .additionalModels(typeResolver.resolve(ErrorResponseEnvelope.class),
                        typeResolver.resolve(ResponseEnvelope.class),
                        typeResolver.resolve(ValidationEnvelope.class))
                .securitySchemes(authConfig.getApiKeys())
                .securityContexts(authConfig.getSecurityContext());
    }

}
