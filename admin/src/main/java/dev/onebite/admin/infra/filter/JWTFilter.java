package dev.onebite.admin.infra.filter;

import dev.onebite.admin.application.dto.AuthToken;
import dev.onebite.admin.domain.AdminCredentialProvider;
import dev.onebite.admin.infra.util.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AdminCredentialProvider adminCredentialProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null) {
            try {
                String keyString = adminCredentialProvider.getJwtKey().key();


                if (jwtTokenProvider.validateToken(token, keyString)) {

                    String adminId = jwtTokenProvider.getAdminId(token, keyString);
                    Authentication auth = new UsernamePasswordAuthenticationToken(adminId, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    long remainingTime = jwtTokenProvider.getRemainingTime(token, keyString);

                    if (remainingTime < 1000 * 60 * 5) {
                        log.info("토큰 만료가 5분 미만으로 남았습니다. 자동으로 재발급합니다.");

                        AuthToken newAuthToken = jwtTokenProvider.createAccessToken(adminId, keyString);

                        response.setHeader("Authorization-Update", newAuthToken.accessToken());
                    }
                }
            }catch (ExpiredJwtException e) {
                log.info("만료된 토큰입니다.");
                request.setAttribute("exception", "EXPIRED_TOKEN");
            } catch (Exception e) {
                log.error("JWT 인증 실패: {}", e.getMessage());
                request.setAttribute("exception", "INVALID_TOKEN");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}