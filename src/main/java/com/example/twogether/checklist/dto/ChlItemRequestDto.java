package com.example.twogether.checklist.dto;

import com.example.twogether.checklist.entity.CheckList;
import com.example.twogether.checklist.entity.CheckListItem;
import lombok.Getter;

@Getter
public class ChlItemRequestDto {

    private String content;
    //private Boolean isChecked;

    public CheckListItem toEntity(CheckList checkList) {
        return CheckListItem.builder()
            .content(content)
            //.isChecked(isChecked)
            .checkList(checkList)
            .build();
    }

}
