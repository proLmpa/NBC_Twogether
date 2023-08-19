package com.example.twogether.common.error;

import lombok.Getter;

@Getter
public enum CustomErrorCode {
    // User
    USER_NOT_FOUND("U001", "존재하지 않는 사용자입니다."),
    UNAUTHORIZED_REQUEST("U002", "승인되지 않은 요청입니다."),
    PASSWORD_MISMATCHED("U003", "기존 비밀번호와 일치하지 않습니다."),
    PASSWORD_RECENTLY_USED("U004", "최근 2회 이내에 사용한 적 있는 비밀번호입니다."),

    // Email
    EMAIL_ALREADY_USED("EM001", "이미 사용 중인 이메일입니다."),
    EMAIL_SEND_FAILED("EM002", "이메일 전송에 실패했습니다"),
    EMAIL_NOT_FOUND("EM003", "인증을 요청받은 메일이 아닙니다."),
    VERIFY_NOT_ALLOWED("EM004", "이메일 인증 번호가 일치하지 않습니다.");

    private final String errorCode;
    private final String errorMessage;

    CustomErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
