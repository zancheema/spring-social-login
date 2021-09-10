package com.example.social.login.security;

import java.util.Date;

import com.example.social.login.model.User;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class TokenProvider {
    private static final long EXPIRE_DURATION_MILLIS = 864000000;

    public String createToken(User user) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + EXPIRE_DURATION_MILLIS);

        return Jwts.builder()
            .setSubject(user.getId())
            .setIssuedAt(issuedAt)
            .setExpiration(expiresAt)
            .compact();
    }
}
