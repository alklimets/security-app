package com.fm.music.application.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

@Component
public class JwtUtil {
    private PublicKey accessTokenPublicKey;
    private PrivateKey accessTokenPrivateKey;
    private PublicKey refreshTokenPublicKey;
    private PrivateKey refreshTokenPrivateKey;

    @Value("${jwt.access.token.ttl}")
    private String accessTokenTtl;

    @Value("${jwt.refresh.token.ttl}")
    private String refreshTokenTtl;

    private PemObject readPemFile(String filename) throws IOException {
        try (Reader reader = new FileReader(filename); PemReader pemReader = new PemReader(reader)) {
            return pemReader.readPemObject();
        }
    }

    public  <T> T extractClaim(Claims claims, Function<Claims, T> extract) {
        return extract.apply(claims);
    }

    public Claims extractAccessClaims(String token) {
        return extractTokenClaims(token, accessTokenPublicKey);
    }

    public Claims extractRefreshClaims(String token) {
        return extractTokenClaims(token, refreshTokenPublicKey);
    }

    private Claims extractTokenClaims(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }

    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenPrivateKey, accessTokenTtl);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenPrivateKey, refreshTokenTtl);
    }

    private String generateToken(String username, PrivateKey key, String ttl) {
        Claims claims = Jwts.claims().setSubject(username);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.RS256, key)
                .setExpiration(new Date(new Date().getTime() + TimeUnit.HOURS.toMillis(parseInt(ttl))))
                .compact();
    }

    public JwtUtil() throws Exception {
        PrivateKey privateKey = getPrivateKey("application/src/main/resources/keys/private_key.pem");
        PublicKey publicKey = getPublicKey("application/src/main/resources/keys/public_key.pem");
        this.accessTokenPublicKey = publicKey;
        this.accessTokenPrivateKey = privateKey;
        this.refreshTokenPublicKey = publicKey;
        this.refreshTokenPrivateKey = privateKey;
    }

    private PrivateKey getPrivateKey(String filename) throws Exception {
        PemObject pemObject = readPemFile(filename);
        byte[] content = pemObject.getContent();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(content);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private PublicKey getPublicKey(String filename) throws Exception {
        PemObject pemObject = readPemFile(filename);
        byte[] content = pemObject.getContent();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(content);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
