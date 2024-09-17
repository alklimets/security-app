package com.aklimets.pet.infrasctucture.cryptography;

import com.aklimets.pet.crypto.util.AsymmetricKeyUtil;
import com.aklimets.pet.infrasctucture.security.keyprovider.JwtKeyPairProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyPairConfiguration {

    @Value("${jwt.access.public.key.path}")
    private String accessPublicKeyPath;

    @Value("${jwt.refresh.public.key.path}")
    private String refreshPublicKeyPath;

    @Value("${jwt.access.private.key.path}")
    private String accessPrivateKeyPath;

    @Value("${jwt.refresh.private.key.path}")
    private String refreshPrivateKeyPath;

    @Autowired
    private AsymmetricKeyUtil asymmetricKeyUtil;

    @Bean
    public JwtKeyPairProvider jwtAccessKeyPairProvider() {
        return new JwtKeyPairProvider(accessPublicKeyPath, accessPrivateKeyPath, asymmetricKeyUtil);
    }

    @Bean
    public JwtKeyPairProvider jwtRefreshKeyPairProvider() {
        return new JwtKeyPairProvider(refreshPublicKeyPath, refreshPrivateKeyPath, asymmetricKeyUtil);
    }

}
