package com.example.twogether.board.service;

import com.example.twogether.board.dto.BoardMemberRequestDto;
import com.example.twogether.board.dto.BoardResponseDto;
import com.example.twogether.board.dto.BoardsResponseDto;
import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardMember;
import com.example.twogether.board.repository.BoardMemberRepository;
import com.example.twogether.board.repository.BoardRepository;
import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.common.security.UserDetailsImpl;
import com.example.twogether.user.entity.User;
import com.sun.jdi.request.DuplicateRequestException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardMemberService {

    private final BoardMemberRepository boardMemberRepository;
    private final BoardRepository boardRepository;

    // 보드 협업자 초대 - 허락받아야 초대되는 로직으로 develop 할지 고민 중
    @Transactional
    public void addBoardMember(Long boardId, Long boardMemberId) {

        try {
            Board foundBoard = findBoard(boardId);
            BoardMember foundBoardMember = findByBoard_Id_and_BoardMember_Id(foundBoard, boardMemberId);

            BoardMember boardMember = BoardMemberRequestDto.toEntity(collaborator, board);
            boardMemberRepository.save(boardMember);
        } catch (Exception e) {
            throw new RuntimeException("협업자 등록에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 협업 초대받은 보드 협업자 명단 수정
    @Transactional
    public void updateCollaborator(Board board, BoardMember boardMember, User newCollaborator) {
        try {
            if (!boardMember.getBoard().equals(board)) {
                throw new IllegalArgumentException("해당 칸반 보드의 협업자가 아닙니다.");
            }
            //명단에 내가 이미 있어서 또 초대할 필요가 없는지 확인
            if (boardMember.getBoard().getBoardMembers().stream()
                .anyMatch(user -> user.getCollaborator().equals(newCollaborator))) {
                throw new DuplicateRequestException("이미 협업자로 할당된 사용자입니다.");
            }
            boardMember.updateCollaborator(newCollaborator);
        } catch (Exception e) {
            throw new RuntimeException("협업자 수정에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 협업 초대받은 보드 협업자 삭제
    @Transactional
    public void deleteCollaborator(Long boardId, Long userId) {
        BoardMember boardMember = boardMemberRepository.findByBoard_IdAndCollaborator_Id(boardId, userId)
            .orElseThrow(() -> new IllegalArgumentException("해당 칸반 보드의 협업자가 아닙니다."));
        try {
            boardMemberRepository.delete(boardMember);
        } catch (Exception e) {
            throw new RuntimeException("협업자 삭제에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 협업 초대받은 보드 전체 조회
    @Transactional(readOnly = true)
    public BoardsResponseDto getCollaboratedBoards(UserDetailsImpl userDetails) {
        try {
            List<BoardMember> boardMembers = boardMemberRepository.findByBoardMember(
                userDetails.getUser());

            List<Board> collaboratedBoards = boardMembers.stream()
                .map(BoardMember::getBoard)
                .collect(Collectors.toList());

            return BoardsResponseDto.of(collaboratedBoards);
        } catch (Exception e) {
            throw new RuntimeException("협업 중인 칸반 보드 조회를 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 협업 초대받은 보드 단건 조회
    @Transactional(readOnly = true)
    public BoardResponseDto getCollaboratedBoardById(User boardCollabo, Long id) {
        try {
            Board board = findBoard(boardCollabo, id);
            return BoardResponseDto.of(board);
        } catch (Exception e) {
            throw new RuntimeException("협업 중인 칸반 보드 조회를 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    // 내 칸반 보드의 협업자 명단 조회
    public BoardMembersResponseDto getBoardMembers(Long id) {
        List<BoardMember> boardList = boardMemberRepository.findByBoard_Id(id);

        return BoardMembersResponseDto.of(boardList);
    }

    private BoardMember findCollaborator(Long boardMemberId) {
        return boardMemberRepository.findById(boardMemberId)
            .orElseThrow(() -> new NotFoundException("해당 칸반 보드에 존재하지 않는 협업자입니다."));
    }

    private Board findBoard(Long boardId) {
        // 존재하지 않는 보드를 찾는 경우 예외 던지기
        return boardRepository.findById(boardId).orElseThrow(() -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND));
    }

    private BoardMember findByBoard_Id_and_BoardMember_Id(Board foundBoard, Long boardMemberId) {

        // 해당 보드에 이미 등록된 협업자인 경우 예외 던지기
        if(boardMemberRepository.existsByBoardAndBoardMember_Id(foundBoard, boardMemberId)) {
            throw new CustomException(CustomErrorCode.BOARD_MEMBER_ALREADY_EXISTS);
        }
        else {
            return boardMemberRepository.findByBoardANDBoardMember_Id(foundBoard, boardMemberId);
        }
    }
}
