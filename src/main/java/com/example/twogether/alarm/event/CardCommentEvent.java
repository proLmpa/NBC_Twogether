package com.example.twogether.alarm.event;

import com.example.twogether.card.entity.Card;
import com.example.twogether.comment.entity.Comment;
import com.example.twogether.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.context.ApplicationEvent;

@Getter
public class CardCommentEvent extends ApplicationEvent {

    private final User loginUser;
    private final Card card;
    private final Comment comment;
    private final String content;

    @Builder
    public CardCommentEvent(Object source, User user, Card card, Comment comment) {
        super(source);
        this.loginUser = user;
        this.card = card;
        this.comment = comment;
        this.content = generateContent(loginUser, card, comment);
    }

    private String generateContent(User loginUser, Card card, Comment comment) {

        return loginUser.getNickname() + "님이 작업자로 할당되어 있는 "
            + "[ID" + card.getId() + ". " + card.getTitle() + "] 카드에 "
            + "댓글 [ID" + comment.getId() + ". " + comment.getContent() + "가 달렸습니다.";
    }
}
