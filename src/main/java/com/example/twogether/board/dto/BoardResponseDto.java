package com.example.twogether.board.dto;

import com.example.twogether.board.entity.Board;
import com.example.twogether.common.dto.ApiResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class BoardResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String color;
    private String info;

    public static BoardResponseDto of(Board board) {
        return BoardResponseDto.builder()
            .id(board.getId())
            .nickname(board.getBoardAuthor().getNickname())
            .title(board.getTitle())
            .color(board.getColor())
            .info(board.getInfo())
            .build();
    }
}
