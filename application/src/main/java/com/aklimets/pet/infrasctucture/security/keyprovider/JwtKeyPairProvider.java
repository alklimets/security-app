package com.aklimets.pet.infrasctucture.security.keyprovider;

import com.aklimets.pet.crypto.provider.KeyPairProvider;
import com.aklimets.pet.crypto.util.AsymmetricKeyUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.security.PrivateKey;
import java.security.PublicKey;

import static com.aklimets.pet.crypto.model.AsymmetricAlgorithm.RSA;

@Slf4j
public class JwtKeyPairProvider implements KeyPairProvider {

    private final String publicKeyPath;

    private final String privateKeyPath;

    private final AsymmetricKeyUtil asymmetricKeyUtil;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    public JwtKeyPairProvider(String publicKeyPath, String privateKeyPath, AsymmetricKeyUtil asymmetricKeyUtil) {
        this.publicKeyPath = publicKeyPath;
        this.privateKeyPath = privateKeyPath;
        this.asymmetricKeyUtil = asymmetricKeyUtil;
    }

    @Override
    @SneakyThrows
    public PublicKey getPublicKey() {
        if (publicKey == null) {
            var pemFile = asymmetricKeyUtil.readPemFile(publicKeyPath);
            publicKey = asymmetricKeyUtil.getPublicKeyInstance(pemFile.getContent(), RSA);
        }
        return publicKey;
    }

    @Override
    @SneakyThrows
    public PrivateKey getPrivateKey() {

        if (privateKey == null) {
            var pemFile = asymmetricKeyUtil.readPemFile(privateKeyPath);
            privateKey = asymmetricKeyUtil.getPrivateKeyInstance(pemFile.getContent(), RSA);
        }
        return privateKey;
    }
}
