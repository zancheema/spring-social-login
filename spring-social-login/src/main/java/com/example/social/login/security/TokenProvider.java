package com.example.social.login.security;

import java.util.Date;

import com.example.social.login.config.AppProperties;
import com.example.social.login.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenProvider {
    private static final long EXPIRE_DURATION_MILLIS = 864000000;

    @Autowired
    private AppProperties appProperties;

    public String createToken(User user) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + EXPIRE_DURATION_MILLIS);

        return Jwts.builder()
            .setSubject(user.getId())
            .setIssuedAt(issuedAt)
            .setExpiration(expiresAt)
            .signWith(SignatureAlgorithm.HS256, appProperties.getSecretKey())
            .compact();
    }

    public String getIdFromToken(String token) {
        Claims claims = Jwts.parser()
        .setSigningKey(appProperties.getSecretKey())
        .parseClaimsJws(token)
        .getBody();

        return claims.getSubject();
    }
}
