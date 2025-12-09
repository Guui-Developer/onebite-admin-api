package dev.onebite.admin.infra.util.jwt;


import dev.onebite.admin.application.dto.AuthToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 20;

    public AuthToken createAccessToken(String adminId, String keyString) {
        long now = (new Date()).getTime();
        long expireAt = now + ACCESS_TOKEN_EXPIRE_TIME;
        Date validity = new Date(expireAt);

        Key secretKey = Keys.hmacShaKeyFor(keyString.getBytes(StandardCharsets.UTF_8));

        String accessToken = Jwts.builder()
                .setSubject(adminId)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return new AuthToken(accessToken, ACCESS_TOKEN_EXPIRE_TIME / 1000, expireAt);
    }
}
