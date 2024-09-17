package com.aklimets.pet.infrasctucture.cryptography;

import com.aklimets.pet.crypto.provider.VersionedKeyPairProvider;
import com.aklimets.pet.crypto.util.AsymmetricKeyUtil;
import com.aklimets.pet.crypto.util.SymmetricKeyUtil;
import com.aklimets.pet.infrasctucture.cryptography.keyprovider.JwtKeyPairProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KeyPairConfiguration {

    @Value("${simple.secrets.manager.access-key-name}")
    private String accessKeyName;

    @Value("${simple.secrets.manager.refresh-key-name}")
    private String refreshKeyName;

    @Value("${simple.secrets.manager.url}")
    private String secretsManagerUrl;

    @Autowired
    private AsymmetricKeyUtil asymmetricKeyUtil;

    @Autowired
    private SymmetricKeyUtil symmetricKeyUtil;

    @Autowired
    private RestTemplate secretsManagerRestTemplate;

    @Bean
    public VersionedKeyPairProvider jwtAccessKeyPairProvider() {
        return new JwtKeyPairProvider(accessKeyName, secretsManagerUrl, secretsManagerRestTemplate, asymmetricKeyUtil, symmetricKeyUtil);
    }

    @Bean
    public VersionedKeyPairProvider jwtRefreshKeyPairProvider() {
        return new JwtKeyPairProvider(refreshKeyName, secretsManagerUrl, secretsManagerRestTemplate, asymmetricKeyUtil, symmetricKeyUtil);
    }

}
