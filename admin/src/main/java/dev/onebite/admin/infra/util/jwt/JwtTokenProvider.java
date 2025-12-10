package dev.onebite.admin.infra.util.jwt;


import dev.onebite.admin.application.dto.AuthToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.util.Date;

@Slf4j
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

    public boolean validateToken(String token, String keyString) {
        try {
            parseClaims(token, keyString);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.");
        } catch (MalformedJwtException e) {
            log.warn("유효하지 않은 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String getAdminId(String token, String keyString) {
        return parseClaims(token, keyString).getSubject();
    }

    private Key getSecretKey(String keyString) {
        return Keys.hmacShaKeyFor(keyString.getBytes(StandardCharsets.UTF_8));
    }

    private Claims parseClaims(String token, String keyString) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey(keyString))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getRemainingTime(String token, String keyString) {
        Date expiration = parseClaims(token, keyString).getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }
}
