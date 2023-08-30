package com.example.twogether.notification.entity;

import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.card.entity.Card;
import com.example.twogether.card.entity.CardCollaborator;
import com.example.twogether.user.entity.User;
import com.example.twogether.workspace.entity.WorkspaceCollaborator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    @Enumerated(value = EnumType.STRING)
    private NotificationTitle title;

    @Column
    private String message;

    // 수정된 시간으로부터 지난 시간
    @Column
    private String editedTimeAgo;

    // 변화를 일으킨 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User editor;

    // 알림이 받는 대상
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User receiver;

    // 변동 사항을 감지하는 대상 - 워크스페이스 협업자로 초대된 경우
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private WorkspaceCollaborator workspaceCollaborator;

    // 변동 사항을 감지하는 대상 - 보드 협업자로 초대된 경우
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private BoardCollaborator boardCollaborator;

    // 변동 사항을 감지하는 대상 - 카드에 작업자로 할당 되는 경우
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CardCollaborator cardCollaborator;

    // 변동 사항을 감지하는 대상 - 카드 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Card card;
}
