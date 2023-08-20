/*
package com.example.twogether.board.controller;

import com.example.twogether.board.dto.BoardResponseDto;
import com.example.twogether.board.dto.BoardsResponseDto;
import com.example.twogether.board.service.BoardColService;
import com.example.twogether.common.dto.ApiResponseDto;
import com.example.twogether.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "보드 협업자 API")
@Controller
@RequiredArgsConstructor
public class BoardColController {

    private final BoardColService boardColService;

    // 허락받아야 초대되는 로직으로 develop 할지 고민 중
    @Operation(summary = "칸반 보드에 협업자 초대")
    @PostMapping("/boards/invite/{boardId}/{boardColId}")
    public ResponseEntity<ApiResponseDto> addBoardCol(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long boardId,
        @PathVariable Long wpColId
    ) {
        boardColService.addBoardCol(userDetails.getUser(), boardId, wpColId);

        return ResponseEntity.ok()
            .body(new ApiResponseDto(HttpStatus.OK.value(), "칸반 보드에 협업자가 등록되었습니다."));
    }

    @Operation(summary = "칸반 보드에서 협업자 추방")
    @DeleteMapping("/boards/{id}/invite")
    public ResponseEntity<ApiResponseDto> deleteCollaborator(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long boardId,
        @PathVariable Long userId
    ) {
        boardService.deleteCollaborator(boardId, userId);
        return ResponseEntity.ok()
            .body(new ApiResponseDto(HttpStatus.OK.value(), "칸반 보드에서 협업자를 추방하였습니다."));
    }

    // 보드 단건 조회 (협업 초대 받은 보드)
    @Operation(summary = "협업하고 있는 칸반 보드 단건 조회")
    @GetMapping("/boards/collaborators/{id}")
    public ResponseEntity<BoardResponseDto> getCollaboratedBoardById(
        @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        BoardResponseDto result = boardService.getCollaboratedBoardById(userDetails.getUser(), id);
        return ResponseEntity.ok().body(result);
    }

    // 보드 협업자 전체 조회
    @Operation(summary = "칸반 보드의 협업자 명단 전체 조회")
    @GetMapping("/boards/{id}/invite")
    public ResponseEntity<BoardUsersResponseDto> getBoardUsers(@PathVariable Long boardId) {
        BoardUsersResponseDto boardUser = boardService.getBoardUsers(boardId);

        return ResponseEntity.ok().body(boardUser);
    }

    // 보드 전체 조회 (협업 초대 받은 보드)
    @Operation(summary = "협업하고 있는 칸반 보드 전체 조회")
    @GetMapping("/boards/collaborators")
    public ResponseEntity<BoardsResponseDto> getCollaboratedBoards(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardsResponseDto result = boardService.getCollaboratedBoards(userDetails);
        return ResponseEntity.ok().body(result);
    }
}
*/
