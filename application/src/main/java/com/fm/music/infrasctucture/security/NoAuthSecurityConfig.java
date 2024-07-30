package com.fm.music.infrasctucture.security;

import com.fm.music.infrasctucture.security.annotation.NoAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@NoAuth
public class NoAuthSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers().cacheControl();
        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers("/**").permitAll();
        return http.build();
    }
}
