package com.aklimets.pet.application.util.jwt;

import com.aklimets.pet.domain.dto.jwt.JwtUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtExtractor {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtUser extractAccessJwtUser(String token) {
        var claims = jwtUtil.extractAccessClaims(token);
        return buildJwtUser(claims);
    }

    public JwtUser extractRefreshJwtUser(String token) {
        var claims = jwtUtil.extractRefreshClaims(token);
        return buildJwtUser(claims);
    }

    private JwtUser buildJwtUser(Claims claims) {
        var username = jwtUtil.extractClaim(claims, Claims::getSubject);
        var expiration = jwtUtil.extractClaim(claims, Claims::getExpiration);
        return new JwtUser(username, expiration);
    }
}
