package com.example.twogether.board.dto;

import com.example.twogether.board.entity.BoardMember;
import com.example.twogether.user.entity.User;
import lombok.Getter;

@Getter
public class BoardMemberRequestDto {

    private String email;

    public BoardMember toEntity(User boardAuthor) {
        return BoardMember.builder()
            .
    }
}
