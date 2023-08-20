package com.example.twogether.board.controller;

import com.example.twogether.board.dto.BoardRequestDto;
import com.example.twogether.board.dto.BoardResponseDto;
import com.example.twogether.board.service.BoardService;
import com.example.twogether.common.dto.ApiResponseDto;
import com.example.twogether.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "보드 CRUD API")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    // 보드 생성
    @Operation(summary = "칸반 보드 생성")
    @PostMapping("/boards/{workspaceId}")
    public ResponseEntity<ApiResponseDto> createBoard(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long workspaceId,
        @RequestBody BoardRequestDto boardRequestDto
    ) {

        boardService.createBoard(userDetails.getUser(), workspaceId, boardRequestDto);

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.CREATED.value(), "칸반 보드가 생성되었습니다."));
    }

    // 보드 수정
    @Operation(summary = "칸반 보드 수정", description = "")
    @PatchMapping("/boards/{workspaceId}/{boardId}")
    public ResponseEntity<ApiResponseDto> editBoard(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long workspaceId,
        @PathVariable Long boardId,
        @RequestBody BoardRequestDto boardRequestDto
    ) {

        boardService.editBoard(userDetails.getUser(), workspaceId, boardId, boardRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new ApiResponseDto(HttpStatus.OK.value(), "칸반 보드가 수정되었습니다."));
    }

    // 보드 삭제
    @Operation(summary = "칸반 보드 삭제")
    @DeleteMapping("/boards/{workspaceId}/{boardId}")
    public ResponseEntity<ApiResponseDto> deleteBoard(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long workspaceId,
        @PathVariable Long boardId
    ) {

        boardService.deleteBoard(userDetails.getUser(), workspaceId, boardId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ApiResponseDto(HttpStatus.OK.value(), "칸반 보드 삭제되었습니다."));
    }

    // 보드 단건 조회
    @Operation(summary = "칸반 보드 단건 조회")
    @GetMapping("/boards/{workspaceId}/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoardById(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long workspaceId,
        @PathVariable Long boardId
    ) {

        BoardResponseDto result = boardService.getBoardById(userDetails.getUser(), workspaceId, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}