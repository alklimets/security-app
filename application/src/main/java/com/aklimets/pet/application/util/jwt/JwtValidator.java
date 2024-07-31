package com.aklimets.pet.application.util.jwt;

import com.aklimets.pet.domain.dto.jwt.JwtUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtValidator {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtUser validateAccess(String token) {
        var claims = jwtUtil.extractAccessClaims(token);
        return validate(claims);
    }

    public JwtUser validateRefresh(String token) {
        var claims = jwtUtil.extractRefreshClaims(token);
        return validate(claims);
    }

    private JwtUser validate(Claims claims) {
        JwtUser jwtUser = null;
        try {
            var username = jwtUtil.extractClaim(claims, Claims::getSubject);
            var expiration = jwtUtil.extractClaim(claims, Claims::getExpiration);
            jwtUser = new JwtUser(username, expiration);
        } catch (Exception e) {
            System.out.printf("Error jwt = %s%n", e);
        }
        return jwtUser;
    }
}
