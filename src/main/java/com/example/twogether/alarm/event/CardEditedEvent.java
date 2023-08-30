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

    public static String generateProcessedContent(String oldContent, String newContent) {

        StringBuilder contentBuilder = new StringBuilder();

        int oldIndex = 0;
        int newIndex = 0;

        while (newIndex < newContent.length()) {

            if (oldIndex < oldContent.length() && oldContent.charAt(oldIndex) == newContent.charAt(newIndex)) {
                contentBuilder.append(newContent.charAt(newIndex));
                oldIndex++;
                newIndex++;
            }
            else if (oldContent.charAt(oldIndex) < newContent.charAt(newIndex)) {
                contentBuilder.append("<span style=\"background-color: lightyellow;\">").append(oldContent.charAt(oldIndex)).append("</span>");
                oldIndex++;
            }
            while (newIndex < newContent.length()) {
                contentBuilder.append(newContent.charAt(newIndex));
                newIndex++;
            }
        }

        while (oldIndex < oldContent.length() && oldContent.charAt(oldIndex) != newContent.charAt(newIndex)) {
            contentBuilder.append("<del>").append(oldContent.charAt(oldIndex)).append("</del>");
            oldIndex++;
        }

        String remove = "</del><del>";
        String replace = "";
        int index = contentBuilder.indexOf(remove);

        while (index != -1) {
            contentBuilder.replace(index, index + remove.length(), replace);
            index = contentBuilder.indexOf(remove, index + replace.length());
        }

        return contentBuilder.toString();
    }
}
