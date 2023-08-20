package com.example.twogether.workspace.controller;

import com.example.twogether.common.dto.ApiResponseDto;
import com.example.twogether.common.security.UserDetailsImpl;
import com.example.twogether.workspace.service.WorkspaceMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "워크스페이스 멤버 API", description = "워크스페이스 멤버 API 정보")
public class WorkspaceMemberController {

    private final WorkspaceMemberService workspaceMemberService;

    @Operation(summary = "워크스페이스 협업 멤버 초대")
    @PostMapping("/workspaces/{id}/invite")
    public ResponseEntity<ApiResponseDto> createWorkCollabo(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long workspaceId, @PathVariable Long boardId, @PathVariable Long userId) {
        workspaceMemberService.createWorkCollabo(userDetails.getUser(), workspaceId, boardId, userId);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "Workspace Collaborator Member Creation Success!"));
    }
    @Operation(summary = "워크스페이스 협업 멤버 삭제")
    @DeleteMapping("/workspaces/{id}/invite")
    public ResponseEntity<ApiResponseDto> deleteWorkCollabo(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long workspaceId, @PathVariable Long boardId, @PathVariable Long userId) {
        workspaceMemberService.outWorkCollabo(userDetails.getUser(), workspaceId, boardId, userId);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "Workspace Collaborator Member Delete Success!"));
    }
}
