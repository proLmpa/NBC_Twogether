package com.example.twogether.alarm.event;

import com.example.twogether.alarm.dto.AlarmRequestDto;
import com.example.twogether.alarm.entity.Alarm;
import com.example.twogether.alarm.entity.AlarmTrigger;
import com.example.twogether.alarm.service.AlarmService;
import com.example.twogether.card.entity.Card;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class TriggerEventListener implements ApplicationListener<CardEditedEvent> {

    private final AlarmService alarmService;

    @Override
    @TransactionalEventListener
    public void onApplicationEvent(CardEditedEvent event) {

        Card card = event.getCard();
        if (!event.getOldContent().equals(event.getNewContent())) {
            log.info("cardEditedEvent() : 카드 내용이 수정되었습니다.");

            Alarm alarm = AlarmRequestDto.toEntity(
                event.getUser(),
                event.getContent(),
                "/api/cards/" + card.getId(),
                AlarmTrigger.CARD_EDITED_EVENT,
                false
            );

            alarmService.createAlarm(alarm);
        }
    }
}
