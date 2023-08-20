package com.example.twogether.workspace.repository;

import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.entity.WorkspaceCollaborator;
import java.util.Optional;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WpColRepository extends JpaRepository<WorkspaceCollaborator, Long> {
    boolean existsByWorkspaceAndEmail(Workspace foundWorkspace, String email);
    Optional<WorkspaceCollaborator> findByWorkspaceAndEmail(Workspace foundWorkspace, String email);
}