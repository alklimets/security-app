package com.aklimets.pet.infrasctucture.security;

import com.aklimets.pet.infrasctucture.security.entrypoint.AuthEntrypoint;
import com.aklimets.pet.infrasctucture.security.provider.BasicAuthProvider;
import com.aklimets.pet.infrasctucture.security.annotation.WithBasicAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static com.aklimets.pet.infrasctucture.security.constants.SecurityConstants.WHITE_LIST_URLS;

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
public class BasicSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicSecurityConfig.class);

    @Autowired
    private BasicAuthProvider basicAuthProvider;

    @Autowired
    private AuthEntrypoint authEntrypoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        LOGGER.info("Basic security config has been activated");
        http.headers().cacheControl();
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(WHITE_LIST_URLS).permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(basicAuthProvider) // basic auth filter calls authManager -> authManager calls provider to authenticate
                .exceptionHandling().authenticationEntryPoint(authEntrypoint)
                .and()
                .httpBasic();
        return http.build();
    }
}
