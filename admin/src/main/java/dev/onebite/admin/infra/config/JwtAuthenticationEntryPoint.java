package dev.onebite.admin.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        String exceptionCode = (String) request.getAttribute("exception");

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> responseMap = buildErrorResponseMap(exceptionCode);

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(responseMap));
    }

    private Map<String, Object> buildErrorResponseMap(String exceptionCode) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", false);

        Map<String, Object> errorMap = new HashMap<>();

        if ("EXPIRED_TOKEN".equals(exceptionCode)) {
            errorMap.put("code", "TOKEN_EXPIRED");
            errorMap.put("message", "토큰이 만료되었습니다. 다시 로그인해주세요.");
        } else {
            errorMap.put("code", "UNAUTHORIZED");
            errorMap.put("message", "인증이 필요합니다. Authorization 헤더를 확인해주세요.");
        }
        responseMap.put("error", errorMap);
        return responseMap;
    }
}
