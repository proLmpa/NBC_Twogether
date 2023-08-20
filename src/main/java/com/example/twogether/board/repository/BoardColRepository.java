package com.example.twogether.board.repository;

import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.workspace.entity.Workspace;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardColRepository extends JpaRepository<BoardCollaborator, Long> {

    List<BoardCollaborator> findByBoard(Board foundBoard);
    boolean existsByUserEmailAndBoard(String email, Board foundBoard);
    boolean existsByBoardAndEmail(Board foundBoard, String email);
    Optional<BoardCollaborator> findByBoardAndEmail(Board foundBoard, String email);
}
