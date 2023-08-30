package com.example.twogether.alarm.dto;

import com.example.twogether.alarm.entity.Alarm;
import com.example.twogether.alarm.entity.AlarmTrigger;
import com.example.twogether.user.entity.User;

public class AlarmRequestDto {

    public static Alarm toEntity(User user, String content, String url, AlarmTrigger alarmTrigger, boolean isRead) {

        return Alarm.builder()
            .content(content)
            .url(url)
            .alarmTrigger(alarmTrigger)
            .isRead(isRead)
            .receiver(user)
            .build();
    }
}
