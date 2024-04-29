package com.pintor.product_module.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.expiration.access_token}")
    private Long accessTokenExpiration;

    @Value("${jwt.expiration.refresh_token}")
    private Long refreshTokenExpiration;

    @Value("${jwt.secret.key}")
    private String originalKey;

    private SecretKey secretKey;

    private SecretKey genSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(this.originalKey.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    private SecretKey getSecretKey() {
        if (this.secretKey == null) this.secretKey = this.genSecretKey();
        return this.secretKey;
    }

    public String genAccessToken(Member member) {
        Claims claims = Jwts.claims()
                .add("id", member.getId())
                .add("email", member.getEmail())
                .add("role", member.getRole().name())
                .build();

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(now))
                .expiration(new Date(now + 1000 * this.accessTokenExpiration))
                .signWith(this.getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String genRefreshToken() {
        Claims claims = Jwts.claims().build();

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(now))
                .expiration(new Date(now + 1000 * this.refreshTokenExpiration))
                .signWith(this.getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getMemberId(String accessToken) {

        try {
            return Jwts.parser()
                    .verifyWith(this.getSecretKey())
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload()
                    .get("id", Long.class);
        } catch (ExpiredJwtException e) {
            return -1L;
        } catch (Exception e) {
            return -2L;
        }
    }
}
