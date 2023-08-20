package com.example.twogether.workspace.dto;

import com.example.twogether.user.entity.User;
import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.entity.WorkspaceCollaborator;
import lombok.Getter;

@Getter
public class WpColRequestDto {

    private Long id;
    private String email;

    public static WorkspaceCollaborator toEntity(User wpCol, Workspace workspace) {
        return WorkspaceCollaborator.builder()
            .id(wpCol.getId())
            .email(wpCol.getEmail())
            .user(wpCol)
            .workspace(workspace)
            .build();
    }
}
