package com.example.twogether.board.repository;

import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardColRepository extends JpaRepository<BoardCollaborator, Long> {
    boolean existsByBoardColEmailAndBoard(String email, Board foundBoard);
}
