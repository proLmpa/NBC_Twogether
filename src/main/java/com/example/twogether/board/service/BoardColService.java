package com.example.twogether.board.service;

import com.example.twogether.board.dto.BoardColRequestDto;
import com.example.twogether.board.dto.BoardResponseDto;
import com.example.twogether.board.dto.BoardsResponseDto;
import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.board.repository.BoardColRepository;
import com.example.twogether.board.repository.BoardRepository;
import com.example.twogether.card.entity.Card;
import com.example.twogether.card.entity.CardCollaborator;
import com.example.twogether.card.repository.CardColRepository;
import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.entity.UserRoleEnum;
import com.example.twogether.user.repository.UserRepository;
import com.example.twogether.workspace.dto.WpResponseDto;
import com.example.twogether.workspace.dto.WpsResponseDto;
import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.repository.WpRepository;
import java.util.List;
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
    private final CardColRepository cardColRepository;
    private final UserRepository userRepository;
    private final WpRepository wpRepository;

    // 칸반 보드에 협업자 초대 - 허락받아야 초대되는 로직으로 develop 할지 고민 중
    @Transactional
    public void inviteBoardCol(User user, Long wpId, Long boardId, String email) {

        Board invitingBoard = findBoard(wpId, boardId);
        User invitedUser = findUser(email);

        checkBoardPermissions(invitingBoard, user, email);

        // 이미 등록된 사용자 초대당하기 불가
        if (boardColRepository.existsByBoardAndEmail(invitingBoard, email)) {
            throw new CustomException(CustomErrorCode.BOARD_COLLABORATOR_ALREADY_EXISTS);
        }

        // 보드 협업자로 등록
        BoardCollaborator foundBoardCol = BoardColRequestDto.toEntity(invitedUser, invitingBoard);
        boardColRepository.save(foundBoardCol);
    }

    // 칸반 보드에서 협업자 추방
    @Transactional
    public void outBoardCol(User user, Long wpId, Long boardId, String email) {

        Board invitingBoard = findBoard(wpId, boardId);
        checkBoardPermissions(invitingBoard, user, email);

        // 보드 협업자 삭제 (자동으로 카드에 할당된 협업자 목록에서도 삭제)
        BoardCollaborator foundBoardCol = findBoardColByEmail(invitingBoard, email);
        CardCollaborator foundCardCol = findCardColByEmail(email);
        boardColRepository.delete(foundBoardCol);
        cardColRepository.delete(foundCardCol);
    }

    // 초대된 보드 단건 조회
    @Transactional(readOnly = true)
    public BoardResponseDto getBoardCol(User user, Long wpId, Long boardId) {

        Board foundBoard = findInvitedBoard(wpId, boardId, user.getEmail());
        return BoardResponseDto.of(foundBoard);
    }

    // 초대된 보드 전체 조회
    @Transactional(readOnly = true)
    public BoardsResponseDto getBoardCols(User user, Long wpId) {

        List<Board> foundBoards = findAllInvitedBoards(wpId, user.getEmail());
        return BoardsResponseDto.of(foundBoards);
    }

    private Board findBoard(Long wpId, Long boardId) {

        Workspace foundWorkspace = wpRepository.findById(wpId).orElseThrow(() ->
            new CustomException(CustomErrorCode.WORKSPACE_NOT_FOUND));

        return boardRepository.findByWorkspaceAndId(foundWorkspace, boardId).orElseThrow(() ->
            new CustomException(CustomErrorCode.BOARD_NOT_FOUND));
    }

    private BoardCollaborator findBoardColByEmail(Board board, String email) {

        return boardColRepository.findByBoardAndEmail(board, email).orElseThrow(() ->
            new CustomException(CustomErrorCode.BOARD_COLLABORATOR_NOT_FOUND));
    }

    private CardCollaborator findCardColByEmail(String email) {

        return cardColRepository.findByEmail(email).orElseThrow(() ->
            new CustomException(CustomErrorCode.CARD_COLLABORATOR_NOT_FOUND));
    }

    private List<Board> findAllInvitedBoards(Long wpId, String email) {

        return boardRepository.findAllBoardsByWorkspace_IdAndBoardCollaborators_Email(wpId, email);
    }

    private Board findInvitedBoard(Long wpId, Long boardId, String email) {

        return boardRepository.findByWorkspace_IdAndIdAndAndBoardCollaborators_Email(wpId, boardId, email).orElseThrow(() ->
            new CustomException(CustomErrorCode.BOARD_NOT_FOUND));
    }

    private User findUser(String email) {

        return userRepository.findByEmail(email).orElseThrow(() ->
            new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    private void checkBoardPermissions(Board invitingBoard, User user, String email) {

        if (!invitingBoard.getUser().getId().equals(user.getId()) &&
            !user.getRole().equals(UserRoleEnum.ADMIN)) {

            log.error("보드를 생성한 사람만 협업자 초대/추방할 수 있습니다.");
            throw new CustomException(CustomErrorCode.NOT_YOUR_BOARD);
        }

        if (email.equals(user.getEmail())) { // 추후 프론트에서 예외처리되면 삭제될 예정
            log.error("보드의 오너는 초대/추방할 수 없습니다.");
            throw new CustomException(CustomErrorCode.THIS_IS_YOUR_BOARD);
        }
    }
}