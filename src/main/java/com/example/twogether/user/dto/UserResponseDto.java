package com.example.twogether.user.dto;

import com.example.twogether.user.entity.User;
import com.example.twogether.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String email;
    private UserRoleEnum role;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}