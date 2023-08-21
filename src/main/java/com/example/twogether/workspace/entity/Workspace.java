package com.example.twogether.workspace.entity;

import com.example.twogether.board.entity.Board;
import com.example.twogether.common.entity.Timestamped;
import com.example.twogether.user.entity.User;
import com.example.twogether.workspace.dto.WpRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workspace")
public class Workspace extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "workspace", orphanRemoval = true)
    private List<WorkspaceCollaborator> workspaceCollaborators = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "workspace", orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    public void update(WpRequestDto wpRequestDto) {
        this.title = wpRequestDto.getTitle();
        this.icon = wpRequestDto.getIcon();
    }
}
