package com.example.twogether.common.error;

import lombok.Getter;

@Getter
public enum CustomErrorCode {
    // User
    USER_ALREADY_EXISTS("U001", "이미 존재하는 사용자입니다."),
    USER_NOT_FOUND("U002", "존재하지 않는 사용자입니다."),
    UNAUTHORIZED_REQUEST("U003", "승인되지 않은 요청입니다."),
    PASSWORD_MISMATCHED("U004", "기존 비밀번호와 일치하지 않습니다."),
    PASSWORD_RECENTLY_USED("U005", "최근 2회 이내에 사용한 적 있는 비밀번호입니다."),
    LOGIN_REQUIRED("U006", "로그인 후 사용 가능한 기능입니다."),

    // Workspace
    WORKSPACE_NOT_FOUND("W001", "존재하지 않는 워크스페이스 입니다."),
    NOT_YOUR_WORKSPACE("W003", "본인이 작성한 워크스페이스만 수정/삭제 할 수 있습니다."),

    // board
    BOARD_MEMBER_ALREADY_EXISTS("BM001", "이미 존재하는 보드 협업자입니다."),
    BOARD_NOT_FOUND("B002", "존재하지 않는 보드입니다."),
    NOT_YOUR_BOARD("B003", "본인이 작성한 보드만 삭제 할 수 있습니다."),
    BOARD_NOT_ACCESSIBLE("B004", "해당 보드에 접근할 수 없습니다."),

    // boardCollaborator
    BOARD_COLLABORATOR_NOT_FOUND("BM002", "존재하지 않는 보드 협업자입니다.");

    private final String errorCode;
    private final String errorMessage;

    CustomErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
