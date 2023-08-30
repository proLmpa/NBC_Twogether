package com.example.twogether.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AlarmContentResponseDto {

    private String content;

    public AlarmContentResponseDto(String content) {
        this.content = content;
    }
}
