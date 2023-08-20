package com.example.twogether.board.dto;

import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.user.entity.User;
import lombok.Getter;

@Getter
public class BoardColRequestDto {

    private String userId;

    public static BoardCollaborator toEntity(User boardCol, Board board) {
        return BoardCollaborator.builder()
            .id(boardCol.getId())
            .user(boardCol)
            .board(board)
            .build();
    }
}
