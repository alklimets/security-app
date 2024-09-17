package com.aklimets.pet.infrasctucture.cryptography.keyprovider;

import com.aklimets.pet.crypto.model.AsymmetricAlgorithm;
import com.aklimets.pet.crypto.provider.KeyPairProvider;
import com.aklimets.pet.crypto.provider.VersionedKeyPairProvider;
import com.aklimets.pet.crypto.util.AsymmetricKeyUtil;
import com.aklimets.pet.crypto.util.SymmetricKeyUtil;
import com.aklimets.pet.domain.dto.key.*;
import com.aklimets.pet.domain.exception.NotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtKeyPairProvider implements VersionedKeyPairProvider {

    private final String keyName;

    private final RestTemplate restTemplate;

    private final String secretsBaseUri;

    private final AsymmetricKeyUtil asymmetricKeyUtil;

    private final SymmetricKeyUtil symmetricKeyUtil;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    private Map<String, PublicKey> publicKeyVersions = new HashMap<>();

    private Map<String, PrivateKey> privateKeyVersions = new HashMap<>();

    public JwtKeyPairProvider(String keyName, String secretsBaseUri, RestTemplate restTemplate, AsymmetricKeyUtil asymmetricKeyUtil, SymmetricKeyUtil symmetricKeyUtil) {
        this.keyName = keyName;
        this.secretsBaseUri = secretsBaseUri;
        this.restTemplate = restTemplate;
        this.asymmetricKeyUtil = asymmetricKeyUtil;
        this.symmetricKeyUtil = symmetricKeyUtil;
    }

    @Override
    @SneakyThrows
    public PublicKey getPublicKey() {
        if (publicKey == null) {
            populateKeys();
        }
        return publicKey;
    }

    @Override
    @SneakyThrows
    public PrivateKey getPrivateKey() {
        if (privateKey == null) {
            populateKeys();
        }
        return privateKey;
    }

    @Override
    public PublicKey getPublicKey(String version) {
        if (publicKeyVersions.isEmpty() || !publicKeyVersions.containsKey(version)) {
            populateKeys();
        }
        return publicKeyVersions.get(version);
    }

    @Override
    public PrivateKey getPrivateKey(String version) {
        if (privateKeyVersions.isEmpty() || !privateKeyVersions.containsKey(version)) {
            populateKeys();
        }
        return privateKeyVersions.get(version);
    }

    @SneakyThrows
    private void populateKeys() {
        StoredKeyResponse storedKey = getStoredKey();
        var currentVersion = getCurrentVersion(storedKey);
        var publicKeyResponse = getPublicKeyResponse(currentVersion);
        savePublicKey(publicKeyResponse, currentVersion);

        var sessionKey = symmetricKeyUtil.generateSessionKey();
        var encryptedSessionKey = asymmetricKeyUtil.encrypt(sessionKey, publicKeyResponse.getDisposableKey().publicKey());

        var privateKeyResponse = getPrivateKeyResponse(currentVersion, publicKeyResponse.getDisposableKey().keyId(), encryptedSessionKey);
        savePrivateKey(privateKeyResponse, currentVersion, sessionKey);
    }

    private StoredKeyResponse getStoredKey() {
        return restTemplate.exchange(
                secretsBaseUri + "/api/v1/keys/" + keyName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<SecretsResponseWrapper<StoredKeyResponse>>() {
                }).getBody().getData();
    }

    private PublicKeyResponse getPublicKeyResponse(String currentVersion) {
        return restTemplate.exchange(
                secretsBaseUri + "/api/v1/keys/" + keyName + "/public/" + currentVersion + "?includeDisposableKey=true",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<SecretsResponseWrapper<PublicKeyResponse>>() {
                }).getBody().getData();
    }

    private static String getCurrentVersion(StoredKeyResponse storedKey) {
        return storedKey.versions().stream()
                .filter(v -> v.state().equals("CURRENT"))
                .map(StoredKeyVersionResponse::id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found", "Current key version not found"));
    }

    @SneakyThrows
    private void savePublicKey(PublicKeyResponse publicKeyResponse, String currentVersion) {
        publicKey = asymmetricKeyUtil.getPublicKeyInstance(publicKeyResponse.getPublicKey(), AsymmetricAlgorithm.valueOf(publicKeyResponse.getAlgorithm()));
        publicKeyVersions.put(currentVersion, publicKey);
    }

    private PrivateKeyResponse getPrivateKeyResponse(String currentKeyVersion, String disposableKeyId, String encryptedSessionKey) {
        var request = new PrivateKeyRequest(encryptedSessionKey, disposableKeyId);
        return restTemplate.exchange(
                secretsBaseUri + "/api/v1/keys/" + keyName + "/private/" + currentKeyVersion,
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<SecretsResponseWrapper<PrivateKeyResponse>>() {
                }).getBody().getData();
    }

    @SneakyThrows
    private void savePrivateKey(PrivateKeyResponse privateKeyResponse, String currentVersion, String sessionKey) {
        var decryptedPrivateKey = symmetricKeyUtil.decrypt(privateKeyResponse.privateKey(), sessionKey);
        privateKey = asymmetricKeyUtil.getPrivateKeyInstance(decryptedPrivateKey, AsymmetricAlgorithm.valueOf(privateKeyResponse.algorithm()));
        privateKeyVersions.put(currentVersion, privateKey);
    }
}
