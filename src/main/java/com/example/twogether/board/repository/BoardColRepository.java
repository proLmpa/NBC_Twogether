package com.example.twogether.board.repository;

import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardColRepository extends JpaRepository<BoardCollaborator, Long> {

    List<BoardCollaborator> findByBoard(Board foundBoard);

    boolean existsByUserEmailAndBoard(String email, Board foundBoard);
}
