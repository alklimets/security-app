package com.aklimets.pet.infrasctucture.security;

import com.aklimets.pet.application.util.jwt.JwtExtractor;
import com.aklimets.pet.domain.constants.SecurityConstants;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
import com.aklimets.pet.infrasctucture.security.filter.JwtAuthenticationTokenFilter;
import com.aklimets.pet.infrasctucture.security.handler.JwtSuccessHandler;
import com.aklimets.pet.infrasctucture.security.provider.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

/**
 * Configuration enables jwt security for the module
 * In general this app does not need authentication for
 * incoming requests, but for reference as example it is
 * places here, could be activated by choosing jwt-auth
 * spring profile
 */
@Configuration
@EnableWebSecurity
@WithJwtAuth
public class JwtSecurityConfig {


    @Autowired
    private AuthenticationEntryPoint entryPoint;

    @Autowired
    private JwtAuthenticationProvider authenticationProvider;

    @Autowired
    private JwtExtractor jwtExtractor;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter(authenticationManager(),
                new JwtSuccessHandler(),
                jwtExtractor,
                userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers().cacheControl();
        http.cors().and().csrf().disable()
                .authorizeRequests()
                    .antMatchers(SecurityConstants.WHITE_LIST_URLS).permitAll()
                    .antMatchers("/**").authenticated()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                    // httpSecurity has a list of predefined filters, by using addFilter before and after we
                    // can choose any available class, e. g. UsernamePasswordAuthenticationFilter
                    .addFilterAfter(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
