package com.example.twogether.board.dto;

import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.user.entity.User;
import lombok.Getter;

@Getter
public class BoardCollaboratorRequestDto {

    private String email;

    public BoardCollaborator toEntity(User boardAuthor) {
        return BoardCollaborator.builder()
            .
    }
}
