package com.example.twogether.workspace.dto;

import com.example.twogether.user.entity.User;
import com.example.twogether.workspace.entity.Workspace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class WpRequestDto {

    private String title;
    private String icon;

    public Workspace toEntity(User user) {
        return Workspace.builder()
            .title(this.title)
            .icon(this.icon)
            .user(user)
            .build();
    }
}
