/*
package com.example.twogether.board.service;

import com.example.twogether.board.dto.BoardResponseDto;
import com.example.twogether.board.dto.BoardsResponseDto;
import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.board.repository.BoardColRepository;
import com.example.twogether.board.repository.BoardRepository;
import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.common.security.UserDetailsImpl;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.entity.UserRoleEnum;
import com.sun.jdi.request.DuplicateRequestException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardColService {

    private final BoardColRepository boardColRepository;
    private final BoardRepository boardRepository;

    // 보드 협업자 초대 - 허락받아야 초대되는 로직으로 develop 할지 고민 중
    @Transactional
    public void addBoardCol(User boardAuthor, Long boardId, Long boardColId) {

        try {
            Board foundBoard = findBoard(boardId);

            // 보드를 생성한 사람만 협업자 초대 가능
            if (!foundBoard.getBoardAuthor().getId().equals(boardAuthor.getId()) ||boardAuthor.getRole().equals(
                UserRoleEnum.ADMIN)) {
                throw new CustomException(CustomErrorCode.NOT_YOUR_BOARD);
            }

            BoardCollaborator foundBoardCol = findByBoard_and_BoardCol_Id(foundBoard, boardColId);
            boardColRepository.save(foundBoardCol);
        } catch (Exception e) {
            throw new CustomException(CustomErrorCode.BOARD_COLLABORATOR_NOT_ACCESSIBLE);
        }
    }

    // 협업 초대받은 보드 협업자 명단 수정
    @Transactional
    public void updateBoardCol(User boardAuthor, Long boardId, Long boardColId) {
        try {
            if (!boardCollaborator.getBoard().equals(board)) {
                throw new IllegalArgumentException("해당 칸반 보드의 협업자가 아닙니다.");
            }
            //명단에 내가 이미 있어서 또 초대할 필요가 없는지 확인
            if (boardCollaborator.getBoard().getBoardCollaborators().stream()
                .anyMatch(user -> user.getBoardCol().equals(newCollaborator))) {
                throw new DuplicateRequestException("이미 협업자로 할당된 사용자입니다.");
            }
            boardCollaborator.updateBoardCol(newCollaborator);
        } catch (Exception e) {
            throw new RuntimeException("협업자 수정에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 협업 초대받은 보드 협업자 삭제
    @Transactional
    public void deleteCollaborator(Long boardId, Long userId) {
        BoardCollaborator boardCollaborator = boardColRepository.findByBoard_IdAndCollaborator_Id(boardId, userId)
            .orElseThrow(() -> new IllegalArgumentException("해당 칸반 보드의 협업자가 아닙니다."));
        try {
            boardCollaboratorRepository.delete(boardCollaborator);
        } catch (Exception e) {
            throw new RuntimeException("협업자 삭제에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 협업 초대받은 보드 전체 조회
    @Transactional(readOnly = true)
    public BoardsResponseDto getCollaboratedBoards(UserDetailsImpl userDetails) {
        try {
            List<BoardCollaborator> boardCollaborators = boardColRepository.findByBoardCollaborator(
                userDetails.getUser());

            List<Board> collaboratedBoards = boardCollaborators.stream()
                .map(BoardCollaborator::getBoard)
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
        List<BoardCollaborator> boardList = boardColRepository.findByBoard_Id(id);

        return BoardMembersResponseDto.of(boardList);
    }

    private Board findBoard(Long boardId) {
        // 존재하지 않는 보드를 찾는 경우 예외 던지기
        return boardRepository.findById(boardId).orElseThrow(() -> new CustomException(
            CustomErrorCode.BOARD_NOT_FOUND));
    }

    private BoardCollaborator findByBoard_and_BoardCol_Id(Board foundBoard, Long boardColId) {

        // 해당 보드에 이미 등록된 협업자인 경우 예외 던지기
        if (boardColRepository.existsByBoardAndBoardCollaborator_Id(foundBoard,
            boardColId)) {
            throw new CustomException(CustomErrorCode.BOARD_COLLABORATOR_ALREADY_EXISTS);
        } else {
            return boardColRepository.findByBoardANDBoardCollaborator_Id(foundBoard,
                boardColId);
        }
    }

    private BoardCollaborator findCollaborator(Long boardMemberId) {
        return boardColRepository.findById(boardMemberId)
            .orElseThrow(() -> new NotFoundException("해당 칸반 보드에 존재하지 않는 협업자입니다."));
    }

}
*/
