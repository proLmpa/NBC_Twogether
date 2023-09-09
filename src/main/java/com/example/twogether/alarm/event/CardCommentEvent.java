package com.example.twogether.alarm.event;

import com.example.twogether.alarm.entity.AlarmTarget;
import com.example.twogether.card.entity.Card;
import com.example.twogether.comment.entity.Comment;
import com.example.twogether.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.context.ApplicationEvent;

@Getter
public class CardCommentEvent extends ApplicationEvent {

    private final User editor;
    private final User alarmTarget;
    private final Card card;
    private final Comment comment;
    private final String content;

    @Builder
    public CardCommentEvent(Object source, User editor, User targetUser, Card card, Comment comment) {

        super(source);
        this.editor = editor;
        this.alarmTarget = targetUser;
        this.card = card;
        this.comment = comment;
        this.content = generateContent(editor, card, comment);
    }

    private String generateContent(User editor, Card card, Comment comment) {

        return "[ " + editor.getNickname() + "Updated Comment ]\n\n"
            + "Card Title : " + card.getTitle() + "\n"
            + "Comment Content : " + comment.getContent();
    }
}
