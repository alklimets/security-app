package com.aklimets.pet.infrasctucture.security;

import com.aklimets.pet.application.util.jwt.JwtExtractor;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
import com.aklimets.pet.infrasctucture.security.constants.SecurityConstants;
import com.aklimets.pet.infrasctucture.security.filter.JwtAuthenticationTokenFilter;
import com.aklimets.pet.infrasctucture.security.filter.RequestIdFilter;
import com.aklimets.pet.infrasctucture.security.handler.JwtSuccessHandler;
import com.aklimets.pet.infrasctucture.security.provider.JwtAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JwtSecurityConfig {

    @Autowired
    private AuthenticationEntryPoint entryPoint;

    @Autowired
    private JwtAuthenticationProvider authenticationProvider;

    @Autowired
    private JwtExtractor jwtExtractor;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestIdFilter requestIdFilter;

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
        log.info("JWT security config has been activated");
        http.headers().cacheControl();
        http.cors().and().csrf().disable()
                // this section will be called after jwt filter work, jwt filter provides authentication instance
                // and then this instance will be used there
                .authorizeHttpRequests(auth -> {
                    try {
                        auth
                                .antMatchers(SecurityConstants.WHITE_LIST_URLS).permitAll() // if some endpoints does not require authentication then they should be included in the whitelist
                                .antMatchers("/**").authenticated()
                                .and()
                                .exceptionHandling().authenticationEntryPoint(entryPoint)
                                .and()
                                .sessionManagement()
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .and()
                                .addFilterAfter(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class) // httpSecurity has a list of predefined filters, by using addFilter before and after we  can choose any available class, e. g. UsernamePasswordAuthenticationFilter
                                .addFilterBefore(requestIdFilter, UsernamePasswordAuthenticationFilter.class); // all other urls will require authentication, if jwt filter returns null then 401 error will  be returned automatically, will be called after jwt filter
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return http.build();
    }
}
// if we want to manage authorities directly using spring security we can do it this way,
// list of authorities will be provided by the jwt filter and then this authorities will be
// used, to work with authorities method hasAuthority should be used instead of hasRole
//                           .antMatchers("/api/v1/common/security/authenticate").hasAuthority("ADMIN")
