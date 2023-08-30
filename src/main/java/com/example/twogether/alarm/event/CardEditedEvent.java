package com.example.twogether.alarm.event;

import com.example.twogether.card.entity.Card;
import com.example.twogether.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CardEditedEvent extends ApplicationEvent {

    private final User user;
    private final Card card;
    private final String oldContent;
    private final String newContent;
    private final String content;

    @Builder
    public CardEditedEvent(Object source, User user, Card card, String oldContent, String newContent) {

        super(source);
        this.user = user;
        this.card = card;
        this.oldContent = oldContent;
        this.newContent = newContent;
        this.content = generateProcessedContent(oldContent, newContent);
    }

    // 잘 작동하는지는 프론트에서 확인이 필요함
    public static String generateProcessedContent(String oldContent, String newContent) {

        StringBuilder contentBuilder = new StringBuilder();

        int oldIndex = 0;
        int newIndex = 0;

        while (newIndex < newContent.length()) {
            if (oldIndex < oldContent.length() && oldContent.charAt(oldIndex) == newContent.charAt(newIndex)) {
                contentBuilder.append(newContent.charAt(newIndex));
                oldIndex++;
                newIndex++;
            } else {
                contentBuilder.append("<del>").append(oldContent.charAt(oldIndex)).append("</del>");
                oldIndex++;
            }
        }

        while (oldIndex < oldContent.length()) {
            contentBuilder.append("<del>").append(oldContent.charAt(oldIndex)).append("</del>");
            oldIndex++;
        }

        return contentBuilder.toString();
    }
}
