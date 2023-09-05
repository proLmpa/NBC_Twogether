package com.example.twogether.alarm.dto;

import com.example.twogether.alarm.entity.Alarm;
import com.example.twogether.alarm.entity.AlarmTrigger;
import com.example.twogether.user.entity.User;

public class CardExtraRequestDto {

    public static Alarm toEntity(User editor, User alarmTarget, String content, String url, AlarmTrigger alarmTrigger) {

        return Alarm.builder()
            .eventMaker(editor)
            .eventReceiver(alarmTarget)
            .content(content)
            .url(url)
            .alarmTrigger(alarmTrigger)
            .build();
    }
}
