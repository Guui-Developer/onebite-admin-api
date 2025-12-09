package dev.onebite.admin.persentation.api.v1;

import dev.onebite.admin.application.dto.AuthToken;
import dev.onebite.admin.application.service.AdminAuthService;
import dev.onebite.admin.persentation.dto.request.ApiResponse;
import dev.onebite.admin.persentation.dto.request.LoginRequest;
import dev.onebite.admin.persentation.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
@RestController
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        AuthToken login = adminAuthService.login(request);
        LocalDateTime expireTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(login.accessExpiresAt()),
                ZoneId.of("UTC")
        );
        return ApiResponse.success(new LoginResponse(
                login.accessToken(),
                login.expiresIn(),
                expireTime));
    }
}

