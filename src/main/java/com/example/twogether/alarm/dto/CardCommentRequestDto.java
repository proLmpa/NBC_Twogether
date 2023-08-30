package com.example.twogether.alarm.dto;

import com.example.twogether.alarm.entity.Alarm;
import com.example.twogether.alarm.entity.AlarmTrigger;
import com.example.twogether.user.entity.User;

public class CardCommentRequestDto {

    public static Alarm toEntity(User loginUser, String content, String url, AlarmTrigger alarmTrigger) {

        return Alarm.builder()
            .loginUser(loginUser)
            .content(content)
            .url(url)
            .alarmTrigger(alarmTrigger)
            .build();
    }
}
