package com.example.twogether.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {

    USER_ALREADY_EXISTS("U001", "이미 존재하는 사용자입니다."),


    // Workspace
    WORKSPACE_NOT_FOUND("W001", "존재하지 않는 워크스페이스 입니다.");

    private final String errorCode;
    private final String errorMessage;

    CustomErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
