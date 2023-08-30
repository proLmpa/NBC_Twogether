package com.example.twogether.alarm.event;

import com.example.twogether.card.entity.Card;
import com.example.twogether.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
@RequiredArgsConstructor
public class TriggerEventPublisher {

    public final ApplicationEventPublisher eventPublisher;

    @Async
    public void publishCardEditedEvent(User user, Card card,String oldContent, String newContent) {

        CardEditedEvent event = new CardEditedEvent(this, user, card, oldContent, newContent);
        eventPublisher.publishEvent(event);
    }
}
