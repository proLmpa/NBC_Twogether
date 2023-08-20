package com.example.twogether.workspace.dto;

import com.example.twogether.board.dto.BoardResponseDto;
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
public class WpResponseDto {
    private Long workspaceId;
    private String title;
    private String icon;
    private String user;
    private List<WpColResponseDto> wpCollaborators;
    private List<BoardResponseDto> boards;


    public static WpResponseDto of(Workspace foundWp) {
        return WpResponseDto.builder()
            .workspaceId(foundWp.getId())
            .user(foundWp.getWpAuthor().getEmail())
            .title(foundWp.getTitle())
            .icon(foundWp.getIcon())
            .wpCollaborators(foundWp.getWorkspaceCollaborators().stream().map(WpColResponseDto::of).toList())
            .boards(foundWp.getBoards().stream().map(BoardResponseDto::of).toList())
            .build();
    }
}
