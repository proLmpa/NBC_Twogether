package com.example.twogether.alarm.event;

import com.example.twogether.user.entity.User;
import com.example.twogether.workspace.entity.Workspace;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InvitedWpColEvent extends ApplicationEvent {

    private final User invitingUser;
    private final User invitedUser;
    private final Workspace workspace;
    private final String content;

    @Builder
    public InvitedWpColEvent(Object source, User editor, User invitedUser, Workspace workspace) {
        super(source);
        this.invitingUser = editor;
        this.invitedUser = invitedUser;
        this.workspace = workspace;
        this.content = generateContent(editor, invitedUser, workspace);
    }

    private String generateContent(User editor, User invitedUser, Workspace workspace) {

        return "[ " + editor.getNickname() + "Invited " + invitedUser + " to the Workspace ]\n\n"
            + "Workspace Title " + workspace.getTitle();
    }
}
