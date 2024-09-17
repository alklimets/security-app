package com.aklimets.pet.infrasctucture.security;

import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
import com.aklimets.pet.infrasctucture.security.keyprovider.JwtKeyPairProvider;
import com.aklimets.pet.jwt.util.JwtExtractor;
import com.aklimets.pet.jwt.util.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@WithJwtAuth
public class JwtConfig {

    @Value("${jwt.access.token.ttl}")
    private String accessTokenTtl;

    @Value("${jwt.refresh.token.ttl}")
    private String refreshTokenTtl;

    @Autowired
    private JwtKeyPairProvider jwtAccessKeyPairProvider;

    @Autowired
    private JwtKeyPairProvider jwtRefreshKeyPairProvider;

    @Bean
    public JwtGenerator jwtGenerator() {
        return new JwtGenerator(jwtAccessKeyPairProvider, jwtRefreshKeyPairProvider, accessTokenTtl, refreshTokenTtl);
    }

    @Bean
    public JwtExtractor jwtExtractor() {
        return new JwtExtractor(jwtAccessKeyPairProvider, jwtRefreshKeyPairProvider);
    }
}
