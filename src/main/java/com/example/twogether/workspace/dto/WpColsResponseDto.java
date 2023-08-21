package com.example.twogether.workspace.dto;


import com.example.twogether.workspace.entity.WorkspaceCollaborator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WpColsResponseDto {

    private List<WpColResponseDto> wpCols;

    public static WpColsResponseDto of(List<WorkspaceCollaborator> wpCols) {
        List<WpColResponseDto> wpColsResponseDto = wpCols.stream()
            .map(WpColResponseDto::of)
            .toList();

        return WpColsResponseDto.builder()
            .wpCols(wpColsResponseDto)
            .build();
    }
}
