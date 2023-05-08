package com.fm.music.security.jwt;

import com.fm.music.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtValidator {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtUser validateAccess(String token) {
        Claims claims = jwtUtil.extractAccessClaims(token);
        return validate(claims);
    }

    public JwtUser validateRefresh(String token) {
        Claims claims = jwtUtil.extractRefreshClaims(token);
        return validate(claims);
    }

    private JwtUser validate(Claims claims) {
        JwtUser jwtUser = null;
        try {
            String username = jwtUtil.extractClaim(claims, Claims::getSubject);
            Date expiration = jwtUtil.extractClaim(claims, Claims::getExpiration);
            jwtUser = new JwtUser(username, expiration);
        } catch (Exception e) {
            System.out.printf("Error jwt = %s%n", e);
        }
        return jwtUser;
    }
}
