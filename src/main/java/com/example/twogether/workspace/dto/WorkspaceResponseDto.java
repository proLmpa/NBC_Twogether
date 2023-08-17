package com.example.twogether.workspace.dto;

import com.example.twogether.common.dto.ApiResponseDto;
import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.entity.WorkspaceMember;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceResponseDto {
    private Long workspaceId;
    private String title;
    private String icon;
    private String email;
    private List<WorkspaceMemberResponseDto> workspaceMembers;

    public static WorkspaceResponseDto of(Workspace workspace) {
        return WorkspaceResponseDto.builder()
            .workspaceId(workspace.getId())
            .title(workspace.getTitle())
            .icon(workspace.getIcon())
            .email(workspace.getUser().getEmail())
            //.workspaceMembers
            .build();
    }


}
