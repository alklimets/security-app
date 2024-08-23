package com.aklimets.pet.infrasctucture.security;

import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
import com.aklimets.pet.jwt.util.JwtExtractor;
import com.aklimets.pet.jwt.util.JwtGenerator;
import com.aklimets.pet.jwt.util.JwtKeyReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@WithJwtAuth
public class JwtConfig {

    @Value("${jwt.access.public.key.path}")
    private String accessPublicKeyPath;

    @Value("${jwt.refresh.public.key.path}")
    private String refreshPublicKeyPath;

    @Value("${jwt.access.token.ttl}")
    private String accessTokenTtl;

    @Value("${jwt.refresh.token.ttl}")
    private String refreshTokenTtl;

    @Value("${jwt.access.private.key.path}")
    private String accessPrivateKeyPath;

    @Value("${jwt.refresh.private.key.path}")
    private String refreshPrivateKeyPath;

    @Bean
    public JwtKeyReader jwtKeyReader() {
        return new JwtKeyReader();
    }

    @Bean
    public JwtGenerator jwtGenerator() throws Exception {
        return new JwtGenerator(accessTokenTtl, refreshTokenTtl, accessPrivateKeyPath, refreshPrivateKeyPath, jwtKeyReader());
    }

    @Bean
    public JwtExtractor jwtExtractor() throws Exception {
        return new JwtExtractor(accessPublicKeyPath, refreshPublicKeyPath, jwtKeyReader());
    }
}
