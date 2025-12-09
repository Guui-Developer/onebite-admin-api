package dev.onebite.admin.application.service;


import dev.onebite.admin.application.dto.AuthToken;
import dev.onebite.admin.domain.AdminCredentialProvider;
import dev.onebite.admin.domain.model.AdminCredential;
import dev.onebite.admin.domain.model.JWTCredential;
import dev.onebite.admin.infra.enums.ErrorCode;
import dev.onebite.admin.infra.util.jwt.JwtTokenProvider;
import dev.onebite.admin.persentation.dto.request.LoginRequest;
import dev.onebite.admin.persentation.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminCredentialProvider adminCredentialProvider;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthToken login(LoginRequest request){
        String loginId = request.id();
        String password = request.password();
        AdminCredential adminCredential = adminCredentialProvider.getCredential();

        if(!(adminCredential.matchLoginId(loginId) && adminCredential.matchPassword(password))){
            throw new ApplicationException(ErrorCode.ADMIN_LOGIN_FAIL);
        }

        JWTCredential jwtKey = adminCredentialProvider.getJwtKey();

        return jwtTokenProvider.createAccessToken(loginId,jwtKey.key());
    }

}
