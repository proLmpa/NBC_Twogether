package com.example.twogether.workspace.controller;

import com.example.twogether.common.dto.ApiResponseDto;
import com.example.twogether.common.security.UserDetailsImpl;
import com.example.twogether.workspace.dto.WorkspaceRequestDto;
import com.example.twogether.workspace.dto.WorkspaceResponseDto;
import com.example.twogether.workspace.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "워크스페이스 API", description = "워크스페이스 API 정보")
public class WorkspaceController {

    //private final UserService userService;
    private final WorkspaceService workspaceService;
    @Operation(summary = "워크스페이스 생성") //  description = "새 워크스페이스 생성"
    @PostMapping("/workspaces")
    public ResponseEntity<ApiResponseDto> createWorkspaces(@RequestBody WorkspaceRequestDto workspaceRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) { //@Valid @RequestBody ~~, @AuthenticationPrincipal UserDetailsImpl userDetails)
        workspaceService.createWorkspace(workspaceRequestDto, userDetails.getUser()); //WorkspaceResponseDto workspaceResponseDto = workspaceService.createWorkspace(workspaceRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "Workspace Creation Success!"));
    }



}
