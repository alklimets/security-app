package com.aklimets.pet.infrasctucture.openapi;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("AKlimets API")
                .description("AKlimets pet project security API")
                .version("1.0")
                .contact(apiContact());
    }

    private Contact apiContact() {
        return new Contact()
                .name("Aliaksandr Klimets")
                .email("aliaksandrklimetst@gmail.com")
                .url("https://github.com/aklimets");
    }

    @Bean
    public GroupedOpenApi securityApi() {
        return GroupedOpenApi.builder()
                .group("security")
                .pathsToMatch("/api/v1/security/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("users")
                .pathsToMatch("/api/v1/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi profileApi() {
        return GroupedOpenApi.builder()
                .group("profiles")
                .pathsToMatch("/api/v1/profile/**")
                .build();
    }
}
