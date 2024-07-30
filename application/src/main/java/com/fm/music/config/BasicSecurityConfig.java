package com.fm.music.config;

import com.fm.music.model.annotation.WithBasicAuth;
import com.fm.music.security.BasicAuthEntrypoint;
import com.fm.music.security.BasicAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static com.fm.music.model.constants.SecurityConstants.WHITE_LIST_URLS;

@Configuration
@EnableWebSecurity
@WithBasicAuth
public class BasicSecurityConfig {

    @Autowired
    private BasicAuthProvider basicAuthProvider;

    @Autowired
    private BasicAuthEntrypoint authEntrypoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers().cacheControl();
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(WHITE_LIST_URLS).permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic();
        http.authenticationProvider(basicAuthProvider);
        http.exceptionHandling().authenticationEntryPoint(authEntrypoint);
        return http.build();
    }
}
