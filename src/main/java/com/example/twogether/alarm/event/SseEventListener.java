package com.example.twogether.alarm.event;

import com.example.twogether.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class SseEventListener {
//
//    private static final String TEXT = "새로운 알림이 도착했습니다.";
//    private final AlarmService alarmService;

//    @Async
//    @TransactionalEventListener
//    public void eventHandler(AlarmTrigger alarmTrigger) {
//        if (alarmTrigger == alarmTrigger.UNICAST) {
//            alarmService.sendAlarm(15L, new AlarmContentResponseDto(TEXT));
//        }
//        if (alarmTrigger == alarmTrigger.BROADCAST) {
//            alarmService.sendBroadcast(new NotificationDTO(TEXT));
//        }
//    }
//}