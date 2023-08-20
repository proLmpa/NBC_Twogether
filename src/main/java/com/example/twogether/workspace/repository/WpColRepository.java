package com.example.twogether.workspace.repository;

import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.entity.WorkspaceCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WpColRepository extends JpaRepository<WorkspaceCollaborator, Long> {
    WorkspaceCollaborator findByWorkspaceAndUserId(Workspace foundWorkspace, Long id);

    boolean existsByWorkspaceAndId(Workspace foundWorkspace, Long id);
}