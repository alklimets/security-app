package com.aklimets.pet.infrasctucture.security;

import com.aklimets.pet.infrasctucture.security.annotation.NoAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Default config, enables using no-auth profile
 */
@Configuration
@EnableWebSecurity
@NoAuth
@Slf4j
public class NoAuthSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("No authentication security config has been activated");
        http.headers().cacheControl();
        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers("/**").permitAll();
        return http.build();
    }
}
