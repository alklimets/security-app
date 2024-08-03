package com.aklimets.pet.application.util.jwt;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class JwtKeyReader {

    public PrivateKey getPrivateKey(String privateKeyPath) throws Exception {
        var pemObject = readPemFile(privateKeyPath);
        var content = pemObject.getContent();
        var spec = new PKCS8EncodedKeySpec(content);
        var kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public PublicKey getPublicKey(String publicKeyPath) throws Exception {
        var pemObject = readPemFile(publicKeyPath);
        var content = pemObject.getContent();
        var spec = new X509EncodedKeySpec(content);
        var kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private PemObject readPemFile(String filename) throws IOException {
        try (var reader = new FileReader(filename); var pemReader = new PemReader(reader)) {
            return pemReader.readPemObject();
        }
    }
}
