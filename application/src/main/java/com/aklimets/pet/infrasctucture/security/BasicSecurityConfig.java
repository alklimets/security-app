package com.aklimets.pet.infrasctucture.security;

import com.aklimets.pet.infrasctucture.security.annotation.WithBasicAuth;
import com.aklimets.pet.infrasctucture.security.constants.SecurityConstants;
import com.aklimets.pet.infrasctucture.security.entrypoint.AuthEntrypoint;
import com.aklimets.pet.infrasctucture.security.provider.BasicAuthProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration enables basic security for the module
 * In general this app does not need authentication for
 * incoming requests, but for reference as example it is
 * places here, could be activated by choosing basic-auth
 * spring profile
 */
@Configuration
@EnableWebSecurity
@WithBasicAuth
@Slf4j
public class BasicSecurityConfig {

    @Autowired
    private BasicAuthProvider basicAuthProvider;

    @Autowired
    private AuthEntrypoint authEntrypoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Basic security config has been activated");
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers(SecurityConstants.WHITE_LIST_URLS).permitAll() // if some endpoints does not require authentication then they should be included in the whitelist
                        .anyRequest().authenticated()
        );
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(handler -> handler.authenticationEntryPoint(authEntrypoint));http.authenticationProvider(basicAuthProvider);
        return http.build();
    }
}
