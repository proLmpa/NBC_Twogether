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
    NOT_YOUR_WORKSPACE("W003", "해당 기능은 워크스페이스를 생성한 사람만 접근 할 수 있습니다."),
    NO_BOARDS_IN_THIS_WORKSPACE("W005", "워크스페이스에 존재하는 보드가 없습니다."),

    // WorkspaceCollaborator
    WORKSPACE_COLLABORATOR_ALREADY_EXISTS("WC001", "이미 존재하는 워크스페이스 협업자입니다."),
    WORKSPACE_COLLABORATOR_NOT_ACCESSIBLE("WC004", "워크스페이스의 기능 사용에 실패했습니다."),
    THIS_IS_YOUR_WORKSPACE("WC005", "당신은 워크스페이스의 관리자입니다."),

    // Board
    BOARD_ALREADY_ARCHIVED("B001", "이미 삭제 보관된 보드입니다."),
    BOARD_NOT_FOUND("B002", "존재하지 않는 보드입니다."),
    NOT_YOUR_BOARD("B003", "해당 기능은 보드를 생성한 사람만 접근할 수 있습니다."),
    BOARD_NOT_ACCESSIBLE("B004", "보드의 CRUD 기능 사용에 실패했습니다."),

    // BoardCollaborator
    BOARD_COLLABORATOR_ALREADY_EXISTS("BC001", "이미 존재하는 보드 협업자입니다."),
    BOARD_COLLABORATOR_NOT_FOUND("BC002", "존재하지 않는 보드 협업자입니다."),
    BOARD_COLLABORATOR_ALREADY_OUT("B003", "이미 추방된 보드 협업자입니다."),
    BOARD_COLLABORATOR_NOT_ACCESSIBLE("BC004", "보드의 협업자 기능 사용에 실패했습니다."),
    THIS_IS_YOUR_BOARD("WC005", "당신은 보드의 관리자입니다.");


    private final String errorCode;
    private final String errorMessage;

    CustomErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
