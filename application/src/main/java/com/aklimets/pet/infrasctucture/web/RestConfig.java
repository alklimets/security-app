package com.aklimets.pet.infrasctucture.web;

import com.aklimets.pet.infrasctucture.web.handler.RestTemplateResponseErrorHandler;
import com.aklimets.pet.infrasctucture.web.interceptor.OutgoingSecretsRequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@AllArgsConstructor
public class RestConfig {

    private final OutgoingSecretsRequestInterceptor outgoingRequestInterceptor;

    @Bean
    public RestTemplate secretsManagerRestTemplate() {
        var restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(outgoingRequestInterceptor);
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }
}
