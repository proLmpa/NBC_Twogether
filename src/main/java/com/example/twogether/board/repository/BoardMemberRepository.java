package com.example.twogether.board.repository;

import com.example.twogether.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMemberRepository extends JpaRepository<BoardCollaborator, Long> {

    List<BoardMember> findByBoardCollaborator(User user);

    boolean existsByBoard_IdAndBoardCollaborator_Id(Long boardId, Long id);
}
