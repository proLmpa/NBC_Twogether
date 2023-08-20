package com.example.twogether.board.repository;

import com.example.twogether.board.entity.Board;
import com.example.twogether.workspace.entity.Workspace;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<List<Board>> findAllByWorkspace(Workspace foundWorkspace);

    Optional<Board> findByWorkspaceAndId(Workspace foundWorkspace, Long boardId);

    Optional<Board> findByWorkspace_IdAndId(Long workspaceId, Long boardId);
}
