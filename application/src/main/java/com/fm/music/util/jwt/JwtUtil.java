package com.fm.music.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

@Component
public class JwtUtil {

    @Value("${jwt.access.token.secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh.token.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.access.token.ttl}")
    private String accessTokenTtl;

    @Value("${jwt.refresh.token.ttl}")
    private String refreshTokenTtl;

    public  <T> T extractClaim(Claims claims, Function<Claims, T> getSubject) {
        return getSubject.apply(claims);
    }

    public Claims extractAccessClaims(String token) {
        return Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(token).getBody();
    }

    public Claims extractRefreshClaims(String token) {
        return Jwts.parser().setSigningKey(refreshTokenSecret).parseClaimsJws(token).getBody();
    }

    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenSecret, accessTokenTtl);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenSecret, refreshTokenTtl);
    }

    private String generateToken(String username, String key, String ttl) {
        Claims claims = Jwts.claims().setSubject(username);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, key)
                .setExpiration(new Date(new Date().getTime() + TimeUnit.HOURS.toMillis(parseInt(ttl))))
                .compact();
    }
}
