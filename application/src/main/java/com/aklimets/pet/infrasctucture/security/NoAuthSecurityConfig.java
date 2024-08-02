package com.aklimets.pet.infrasctucture.security;

import com.aklimets.pet.infrasctucture.security.annotation.NoAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class NoAuthSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoAuthSecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        LOGGER.info("No authentication security config has been activated");
        http.headers().cacheControl();
        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers("/**").permitAll();
        return http.build();
    }
}
