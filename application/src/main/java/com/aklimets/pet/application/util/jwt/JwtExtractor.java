package com.aklimets.pet.application.util.jwt;

import com.aklimets.pet.domain.dto.jwt.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.function.Function;

@Component
public class JwtExtractor {

    private final PublicKey accessTokenPublicKey;
    private final PublicKey refreshTokenPublicKey;

    public JwtExtractor(@Value("${jwt.access.public.key.path}") String accessPublicKeyPath,
                        @Value("${jwt.refresh.public.key.path}") String refreshPublicKeyPath,
                        JwtKeyReader jwtKeyReader) throws Exception {
        var accessPublicKey = jwtKeyReader.getPublicKey(accessPublicKeyPath);
        var refreshPublicKey = jwtKeyReader.getPublicKey(refreshPublicKeyPath);
        this.accessTokenPublicKey = accessPublicKey;
        this.refreshTokenPublicKey = refreshPublicKey;
    }

    public JwtUser extractAccessJwtUser(String token) {
        var claims = extractAccessClaims(token);
        return buildJwtUser(claims);
    }

    public JwtUser extractRefreshJwtUser(String token) {
        var claims = extractRefreshClaims(token);
        return buildJwtUser(claims);
    }

    private JwtUser buildJwtUser(Claims claims) {
        var id = extractClaim(claims, Claims::getId);
        var username = extractClaim(claims, Claims::getSubject);
        var expiration = extractClaim(claims, Claims::getExpiration);
        return new JwtUser(id, username, expiration);
    }
    
    private   <T> T extractClaim(Claims claims, Function<Claims, T> extract) {
        return extract.apply(claims);
    }

    private Claims extractAccessClaims(String token) {
        return extractTokenClaims(token, accessTokenPublicKey);
    }

    private Claims extractRefreshClaims(String token) {
        return extractTokenClaims(token, refreshTokenPublicKey);
    }

    private Claims extractTokenClaims(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }
}
