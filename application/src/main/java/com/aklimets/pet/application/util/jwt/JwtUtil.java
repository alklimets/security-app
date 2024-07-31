package com.aklimets.pet.application.util.jwt;

import com.aklimets.pet.domain.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
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
    private final PublicKey accessTokenPublicKey;
    private final PrivateKey accessTokenPrivateKey;
    private final PublicKey refreshTokenPublicKey;
    private final PrivateKey refreshTokenPrivateKey;

    @Value("${jwt.access.token.ttl}")
    private String accessTokenTtl;

    @Value("${jwt.refresh.token.ttl}")
    private String refreshTokenTtl;

    public JwtUtil() throws Exception {
        var privateKey = getPrivateKey();
        var publicKey = getPublicKey();
        this.accessTokenPublicKey = publicKey;
        this.accessTokenPrivateKey = privateKey;
        this.refreshTokenPublicKey = publicKey;
        this.refreshTokenPrivateKey = privateKey;
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

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenPrivateKey, accessTokenTtl);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenPrivateKey, refreshTokenTtl);
    }

    private String generateToken(User user, PrivateKey key, String ttl) {
        var claims = Jwts.claims()
                .setSubject(user.getUsername())
                .setExpiration(generateExpirationDate(ttl));
        claims.put("id", user.getId());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.RS256, key)
                .compact();
    }

    private static Date generateExpirationDate(String ttl) {
        return new Date(new Date().getTime() + TimeUnit.HOURS.toMillis(parseInt(ttl)));
    }

    private PrivateKey getPrivateKey() throws Exception {
        var pemObject = readPemFile("application/src/main/resources/keys/private_key.pem");
        var content = pemObject.getContent();
        var spec = new PKCS8EncodedKeySpec(content);
        var kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private PublicKey getPublicKey() throws Exception {
        var pemObject = readPemFile("application/src/main/resources/keys/public_key.pem");
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
