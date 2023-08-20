package com.example.twogether.board.repository;

import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCollaboratorRepository extends JpaRepository<BoardCollaborator, Long> {

    List<BoardCollaborator> findByBoardMember(User user);

    boolean existsByBoardAndBoardMember_Id(Board foundBoard, Long boardMemberId);
    BoardCollaborator findByBoardANDBoardMember_Id(Board foundBoard, Long boardMemberId);
}
