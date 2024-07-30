package com.fm.music.infrasctucture.security;

import com.fm.music.application.util.jwt.JwtValidator;
import com.fm.music.domain.model.user.UserRepository;
import com.fm.music.infrasctucture.security.annotation.WithJwtAuth;
import com.fm.music.infrasctucture.security.filter.JwtAuthenticationTokenFilter;
import com.fm.music.infrasctucture.security.handler.JwtSuccessHandler;
import com.fm.music.infrasctucture.security.provider.JwtAuthenticationProvider;
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

import static com.fm.music.domain.constants.SecurityConstants.WHITE_LIST_URLS;

@Configuration
@EnableWebSecurity
@WithJwtAuth
public class JwtSecurityConfig {


    @Autowired
    private AuthenticationEntryPoint entryPoint;

    @Autowired
    private JwtAuthenticationProvider authenticationProvider;

    @Autowired
    private JwtValidator jwtValidator;

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
                jwtValidator,
                userRepository);
    }

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
                    .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                    // httpSecurity has a list of predefined filters, by using addFilter before and after we
                    // can choose any available class, e. g. UsernamePasswordAuthenticationFilter
                    .addFilterAfter(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
