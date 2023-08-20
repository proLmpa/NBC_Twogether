package com.example.twogether.board.repository;

import com.example.twogether.board.entity.BoardCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCollaboratorRepository extends JpaRepository<BoardCollaborator, Long> {
/*
    List<BoardCollaborator> findByBoardCollaborator(User user);

    boolean existsByBoardAndBoardCollaborator_Id(Board foundBoard, Long boardCollaboratorId);

    BoardCollaborator findByBoardANDBoardCollaborator_Id(Board foundBoard, Long boardCollaboratorId);*/
}
