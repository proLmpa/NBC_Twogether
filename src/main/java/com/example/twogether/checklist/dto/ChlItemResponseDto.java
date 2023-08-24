package com.example.twogether.checklist.dto;

import com.example.twogether.checklist.entity.CheckListItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChlItemResponseDto {
    private Long chlItemId;
    private String content;
    private Boolean isChecked;

    public static ChlItemResponseDto of(CheckListItem chlItem) {
        return ChlItemResponseDto.builder()
            .chlItemId(chlItem.getId())
            .content(chlItem.getContent())
            .isChecked(chlItem.getIsChecked())
            .build();
    }
}
