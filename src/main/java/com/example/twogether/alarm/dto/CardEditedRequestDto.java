package com.example.twogether.alarm.dto;

import com.example.twogether.alarm.entity.Alarm;
import com.example.twogether.alarm.entity.AlarmTrigger;
import com.example.twogether.user.entity.User;

public class CardEditedRequestDto {

    public static Alarm toEntity(User user, User alarmTarget, String title, String content, String url, AlarmTrigger alarmTrigger, boolean isRead) {

        return Alarm.builder()
            .eventMaker(user)
            .eventReceiver(alarmTarget)
            .title(title)
            .content(content)
            .url(url)
            .alarmTrigger(alarmTrigger)
            .isRead(isRead)
            .build();
    }
}
