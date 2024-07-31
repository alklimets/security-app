package com.aklimets.pet.infrasctucture.security;

import com.aklimets.pet.infrasctucture.security.annotation.NoAuth;
import com.aklimets.pet.infrasctucture.security.handler.JwtSuccessHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger LOGGER = LogManager.getLogger(NoAuthSecurityConfig.class);

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
