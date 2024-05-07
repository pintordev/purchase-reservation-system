package com.pintor.api_gateway.util;

import com.pintor.api_gateway.auth.response.MemberPassport;
import com.pintor.api_gateway.errors.exception.ApiResException;
import com.pintor.api_gateway.response.FailCode;
import com.pintor.api_gateway.response.ResData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

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

    public MemberPassport getMemberPassport(String accessToken) {

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(this.getSecretKey())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getPayload();

            return MemberPassport.of(
                    claims.get("id", Long.class),
                    claims.get("email", String.class),
                    claims.get("role", String.class)
            );
        } catch (ExpiredJwtException e) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.EXPIRED_ACCESS_TOKEN
                    )
            );
        } catch (Exception e) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_ACCESS_TOKEN
                    )
            );
        }
    }
}
