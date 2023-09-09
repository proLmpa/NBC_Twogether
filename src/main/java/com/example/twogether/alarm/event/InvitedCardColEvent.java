package com.example.twogether.alarm.event;

import com.example.twogether.board.entity.Board;
import com.example.twogether.card.entity.Card;
import com.example.twogether.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InvitedCardColEvent extends ApplicationEvent {

    private final User loginUser;
    private final User addedUser;
    private final Card card;
    private final String content;

    @Builder
    public InvitedCardColEvent(Object source, User editor, User invitedUser, Card card) {
        super(source);
        this.loginUser = editor;
        this.addedUser = invitedUser;
        this.card = card;
        this.content = generateContent(editor, invitedUser, card);
    }

    private String generateContent(User editor, User invitedUser, Card card) {

        return "[ " + editor.getNickname() + "Assigned " + invitedUser + " as a Card Worker ]\n\n"
            + "Workspace Title " + card.getDeck().getBoard().getWorkspace().getTitle() + "\n"
            + "Board Title : " + card.getDeck().getBoard().getTitle() + "\n"
            + "Card Title : " + card.getTitle();
    }
}
