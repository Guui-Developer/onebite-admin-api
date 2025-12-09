package dev.onebite.admin.domain;

import dev.onebite.admin.domain.model.AdminCredential;
import dev.onebite.admin.domain.model.JWTCredential;

public interface AdminCredentialProvider {
    AdminCredential getCredential();
    JWTCredential getJwtKey();
}
