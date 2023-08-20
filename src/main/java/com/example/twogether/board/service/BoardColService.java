package com.example.twogether.board.service;

import com.example.twogether.board.dto.BoardColRequestDto;
import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.board.repository.BoardColRepository;
import com.example.twogether.board.repository.BoardRepository;
import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.entity.UserRoleEnum;
import com.example.twogether.user.repository.UserRepository;
import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.repository.WpRepository;
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
    private final UserRepository userRepository;
    private final WpRepository wpRepository;

    // 칸반 보드에 협업자 초대 - 허락받아야 초대되는 로직으로 develop 할지 고민 중
    @Transactional
    public void inviteBoardCol(User boardAuthor, Long wpId, Long boardId, String email) {

        Workspace foundWorkspace = findWorkspace(wpId);
        Board foundBoard = findBoard(foundWorkspace, boardId);

        // 보드를 생성한 사람만 협업자 초대하기 가능
        if (!foundBoard.getBoardAuthor().getId().equals(boardAuthor.getId())
            || boardAuthor.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(CustomErrorCode.NOT_YOUR_BOARD);
        }

        // 보드 오너는 초대당하기 불가
        if (email.equals(boardAuthor.getEmail())) {
            throw new CustomException(CustomErrorCode.THIS_IS_YOUR_BOARD);
        }

        // 이미 등록된 사용자 초대당하기 불가
        if (boardColRepository.existsByBoardAndEmail(foundBoard, email)) {
            throw new CustomException(CustomErrorCode.BOARD_COLLABORATOR_ALREADY_EXISTS);
        }

        // 보드 협업자로 등록
        User findUser = findUser(email);
        BoardCollaborator foundBoardCol = BoardColRequestDto.toEntity(findUser, foundBoard);
        boardColRepository.save(foundBoardCol);
    }

    // 칸반 보드에서 협업자 추방
    @Transactional
    public void outBoardCol(User boardAuthor, Long wpId, Long boardId, String email) {

        Workspace foundWorkspace = findWorkspace(wpId);
        Board foundBoard = findBoard(foundWorkspace, boardId);

        // 보드를 생성한 사람만 협업자 추방하기 가능
        if (!foundBoard.getBoardAuthor().getId().equals(boardAuthor.getId()) || boardAuthor.getRole().equals(UserRoleEnum.ADMIN)) {

            log.error("보드를 생성한 사람만 협업자 추방할 수 있습니다.");
            throw new CustomException(CustomErrorCode.NOT_YOUR_BOARD);
        }

        // 보드 오너는 초대당하기 불가
        if (email.equals(boardAuthor.getEmail())) {
            log.error("보드 오너는 초대할 수 없습니다.");
            throw new CustomException(CustomErrorCode.THIS_IS_YOUR_BOARD);
        }

        // 보드 협업자 삭제
        BoardCollaborator foundBoardCol = findBoardCol(foundBoard, email);
        boardColRepository.delete(foundBoardCol);
    }

    private Workspace findWorkspace(Long wpId) {

        return wpRepository.findById(wpId).orElseThrow(() ->
            new CustomException(CustomErrorCode.WORKSPACE_NOT_FOUND));
    }

    private Board findBoard(Workspace foundWorkspace, Long boardId) {

        return boardRepository.findByWorkspaceAndId(foundWorkspace, boardId).orElseThrow(() ->
            new CustomException(CustomErrorCode.BOARD_NOT_FOUND));
    }

    private BoardCollaborator findBoardCol(Board foundBoard, String email) {

        return boardColRepository.findByBoardAndEmail(foundBoard, email).orElseThrow(() ->
            new CustomException(CustomErrorCode.BOARD_COLLABORATOR_NOT_FOUND));
    }

    private User findUser(String email) {

        return userRepository.findByEmail(email).orElseThrow(() ->
            new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }
}