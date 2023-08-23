package com.example.twogether.workspace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// 중간 테이블
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class WpColWp implements Persistable<WpColWpId> {
    // 복합키 매핑
    @EmbeddedId
    private WpColWpId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", insertable=false, updatable=false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_collaborator_id", insertable=false, updatable=false)
    private WorkspaceCollaborator workspaceCollaborator;

    @CreatedDate
    private LocalDate created;

    public void setParent(Workspace workspace) {
        this.workspace = workspace;
        this.id = new WpColWpId(workspace.getId(), workspaceCollaborator.getId());
    }

    @Override
    public WpColWpId getId() {
        return id;
    }

    // Spring Data JPA 에서는 데이터를 저장하는 save()는
    // 새로운 엔티티라고 판단되면 persist()를 실행하고, 그렇지 않다면 merge()를 호출한다.
    // merge()를 방지하기 위해 새로운 엔티티인지 판별하는 방법으로 created time 을 사용했다.
    @Override
    public boolean isNew() {
        return created == null;
    }
}

// 식별자 클래스
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
class WpColWpId implements Serializable {
    @Column(name = "workspace_id")
    private Long wpId;

    @Column(name = "workspace_collaborator_id")
    private Long wpColId;

    public WpColWpId(Long wpId, Long wpColId) {
        this.wpId = wpId;
        this.wpColId = wpColId;
    }
}
