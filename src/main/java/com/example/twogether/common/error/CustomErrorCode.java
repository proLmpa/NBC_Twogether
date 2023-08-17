package com.example.twogether.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 사용자입니다."),
    UNAUTHORIZED_REQUESET(HttpStatus.BAD_REQUEST.value(), "승인되지 않은 요청입니다."),
    PASSWORD_MISMATCHED(HttpStatus.BAD_REQUEST.value(), "기존 비밀번호와 일치하지 않습니다."),
    PASSWORD_RECENTLY_USED(HttpStatus.BAD_REQUEST.value(), "최근 3회 이내에 사용한 적 있는 비밀번호입니다."),;

    private final int errorCode;
    private final String errorMessage;

    CustomErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
