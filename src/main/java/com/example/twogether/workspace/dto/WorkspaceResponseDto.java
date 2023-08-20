package com.example.twogether.workspace.dto;

import com.example.twogether.board.dto.BoardResponseDto;
import com.example.twogether.board.dto.BoardsResponseDto;
import com.example.twogether.user.dto.UserResponseDto;
import com.example.twogether.workspace.entity.Workspace;
import java.util.List;
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
    private String user;
    //private List<WorkspaceMemberResponseDto> workspaceMembers;
    private List<BoardResponseDto> boards;


    public static WorkspaceResponseDto of(Workspace workspace) {
        return WorkspaceResponseDto.builder()
            .workspaceId(workspace.getId())
            .user(workspace.getUser().getEmail())
            .title(workspace.getTitle())
            .icon(workspace.getIcon())
            //.workspaceMembers(workspace.getWorkspaceMembers().stream().map(workspaceMember -> new UserResponseDto(workspaceMember.getUser())).toList())
            .boards(workspace.getBoards().stream().map(BoardResponseDto::of).toList())
            .build();
    }
}
