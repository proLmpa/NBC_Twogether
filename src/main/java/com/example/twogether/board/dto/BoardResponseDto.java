package com.example.twogether.board.dto;

import com.example.twogether.board.entity.Board;
import com.example.twogether.common.dto.ApiResponseDto;
import com.example.twogether.workspace.dto.WpColResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class BoardResponseDto {
    private Long boardId;
    private String nickname;
    private String title;
    private String color;
    private String info;
    private List<BoardColResponseDto> boardCollaborators;

    public static BoardResponseDto of(Board foundBoard) {
        return BoardResponseDto.builder()
            .boardId(foundBoard.getId())
            .nickname(foundBoard.getBoardAuthor().getNickname())
            .title(foundBoard.getTitle())
            .color(foundBoard.getColor())
            .info(foundBoard.getInfo())
            .boardCollaborators(foundBoard.getBoardCollaborators().stream().map(
                BoardColResponseDto::of).toList())
            .build();
    }
}
