package dev.onebite.admin.infra.util.aws;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.onebite.admin.domain.AdminCredentialProvider;
import dev.onebite.admin.domain.model.AdminCredential;
import dev.onebite.admin.domain.model.JWTCredential;
import dev.onebite.admin.infra.enums.ErrorCode;
import dev.onebite.admin.persentation.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsSecretManagerProvider implements AdminCredentialProvider {

    private final SecretsManagerClient secretsManagerClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.secrets.admin-cert-name}")
    private String adminSecretName;
    @Value("${aws.secrets.jwt-key-name}")
    private String jwtSecretName;

    public AdminCredential getCredential() {
        try {

            GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                    .secretId(adminSecretName)
                    .build();

            GetSecretValueResponse valueResponse = secretsManagerClient.getSecretValue(valueRequest);
            String secretString = valueResponse.secretString();

            JsonNode secretJson = objectMapper.readTree(secretString);
            String storedId = secretJson.get("ADMIN_ID").asText();
            String storedPw = secretJson.get("ADMIN_PASSWORD").asText();

            return new AdminCredential(storedId, storedPw);
        } catch (Exception e) {
            log.error("AWS Secrets Manager 조회 중 오류 발생: {}", e.getMessage());
            throw new ApplicationException(ErrorCode.AWS_ERROR);
        }

    }

    @Override
    public JWTCredential getJwtKey() {
        try {
            GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                    .secretId(jwtSecretName)
                    .build();

            GetSecretValueResponse valueResponse = secretsManagerClient.getSecretValue(valueRequest);
            String secretString = valueResponse.secretString();

            JsonNode secretJson = objectMapper.readTree(secretString);

            String key = secretJson.get("ACCESS_TOKEN_KEY").asText();

            return new JWTCredential(key);
        } catch (Exception e) {
            log.error("AWS Secrets Manager 조회 중 오류 발생: {}", e.getMessage());
            throw new ApplicationException(ErrorCode.AWS_ERROR);
        }
    }
}
