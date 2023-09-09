package com.example.twogether.alarm.event;

import com.example.twogether.board.entity.Board;
import com.example.twogether.user.entity.User;
import com.example.twogether.workspace.entity.Workspace;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InvitedBoardColEvent extends ApplicationEvent {

    private final User invitingUser;
    private final User invitedUser;
    private final Board board;
    private final String content;

    @Builder
    public InvitedBoardColEvent(Object source, User editor, User invitedUser, Board board) {
        super(source);
        this.invitingUser = editor;
        this.invitedUser = invitedUser;
        this.board = board;
        this.content = generateContent(editor, invitedUser, board);
    }

    private String generateContent(User editor, User invitedUser, Board board) {

        return "[ " + editor.getNickname() + "Invited " + invitedUser + " to the Board ]\n\n"
            + "Workspace Title " + board.getWorkspace().getTitle() + "\n"
            + "Board Title : " + board.getTitle();
    }
}
