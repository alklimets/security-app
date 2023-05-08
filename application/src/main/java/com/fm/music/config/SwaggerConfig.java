package com.fm.music.config;

import com.fasterxml.classmate.TypeResolver;
import com.fm.music.beans.AuthConfig;
import com.fm.music.model.response.ErrorResponsePayload;
import com.fm.music.model.response.ResponsePayload;
import com.fm.music.model.response.ValidationPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Profile("swagger-enabled")
public class SwaggerConfig {

    @Autowired
    private TypeResolver typeResolver;

    @Autowired
    private AuthConfig authConfig;

    @Bean
    public Docket securityApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(authConfig.getApiKeys())
                .securityContexts(authConfig.getSecurityContext())
                .groupName("Security API")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v1/common/security/**"))
                .build()
                .additionalModels(typeResolver.resolve(ErrorResponsePayload.class),
                        typeResolver.resolve(ResponsePayload.class),
                        typeResolver.resolve(ValidationPayload.class));
    }



    @Bean
    public Docket profileApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(authConfig.getApiKeys())
                .securityContexts(authConfig.getSecurityContext())
                .groupName("User Details API")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v1/common/details/**"))
                .build()
                .additionalModels(typeResolver.resolve(ErrorResponsePayload.class),
                        typeResolver.resolve(ResponsePayload.class),
                        typeResolver.resolve(ValidationPayload.class));
    }

}
