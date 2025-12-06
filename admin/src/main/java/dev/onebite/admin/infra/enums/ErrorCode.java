package dev.onebite.admin.infra.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_ERROR("필수 필드가 누락되었습니다"),
    DUPLICATED_CODE("중복된 필드입니다."),
    CODE_NOT_FOUND("존재하지 않는 카테고리 그룹입니다."),
    INVALID_CODE_LENGTH("올바르지 않는 코드 길이입니다."),
    ID_NOT_FOUND("올바르지 않는 아이디입니다."),
    DELETE_DATA_NOT_FOUND("삭제할 대상이 존재하지 않습니다.");

    private final String message;

    public String getCode() {
        return this.name();
    }
}
