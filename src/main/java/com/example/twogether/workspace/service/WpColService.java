package com.example.twogether.workspace.service;

import com.example.twogether.board.dto.BoardColRequestDto;
import com.example.twogether.board.entity.Board;
//import com.example.twogether.board.entity.BoardCollaborator;
//import com.example.twogether.board.repository.BoardColRepository;
import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.board.repository.BoardColRepository;
import com.example.twogether.board.repository.BoardRepository;
import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.entity.UserRoleEnum;
import com.example.twogether.user.repository.UserRepository;
import com.example.twogether.workspace.dto.WpColRequestDto;
import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.entity.WorkspaceCollaborator;
import com.example.twogether.workspace.repository.WpColRepository;
import com.example.twogether.workspace.repository.WpRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WpColService {

    private final WpColRepository wpColRepository;
    private final WpRepository wpRepository;
    private final BoardColRepository boardColRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 워크스페이스 협업자 초대 - 보드 협업자로도 자동 초대
    @Transactional
    public void inviteWpCol(User wpAuthor, Long wpId, Long userId) {

        Workspace foundWorkspace = findWorkspace(wpId);

        // 워크스페이스를 생성한 사람만 협업자 초대 가능
        if (!foundWorkspace.getUser().getId().equals(wpAuthor.getId()) || wpAuthor.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(CustomErrorCode.NOT_YOUR_WORKSPACE);
        }

        // 워크스페이스 오너는 초대 불가
        if (userId.equals(wpAuthor.getId())) {
            throw new CustomException(CustomErrorCode.THIS_IS_YOUR_WORKSPACE);
        }

        // 워크스페이스 협업자로 등록
        User foundUser = findUser(foundWorkspace, userId);
        WorkspaceCollaborator foundWpCol = WpColRequestDto.toEntity(foundUser, foundWorkspace);
        wpColRepository.save(foundWpCol);

        // 워크스페이스에 포함되어 있는 모든 보드에 협업자로 자동 등록
        List<Board> foundAllBoards = findAllBoards(foundWorkspace);

        try {
            if (foundAllBoards != null && !foundAllBoards.isEmpty()) {
                for (Board foundBoard : foundAllBoards) {
                    // 해당 보드에 이미 등록된 협업자인 경우 예외 던지기
                    if (boardColRepository.existsByBoardColEmailAndBoard(foundUser.getEmail(), foundBoard)) {
                        throw new CustomException(CustomErrorCode.BOARD_COLLABORATOR_ALREADY_EXISTS);
                    }
                    BoardCollaborator boardCollaborator = BoardColRequestDto.toEntity(foundUser, foundBoard);
                    boardColRepository.save(boardCollaborator);
                }
            }
        } catch(Exception e) {
            log.error("워크스페이스 협업자 초대에 실패했습니다. 이유: ", e.getMessage(), e);
            throw new CustomException(CustomErrorCode.WORKSPACE_COLLABORATOR_NOT_ACCESSIBLE);
        }
    }

    // 워크스페이스 협업자 추방
    @Transactional
    public void outWpCol(User wpAuthor, Long wpId, Long userId) {

        Workspace foundWorkspace = findWorkspace(wpId);

        // 워크스페이스를 생성한 사람만 협업자 추방 가능
        if (!foundWorkspace.getUser().getId().equals(wpAuthor.getId()) || wpAuthor.getRole()
            .equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(CustomErrorCode.NOT_YOUR_WORKSPACE);
        }

        // 워크스페이스 오너는 추방 불가
        if (userId.equals(wpAuthor.getId())) {
            throw new CustomException(CustomErrorCode.THIS_IS_YOUR_WORKSPACE);
        }

        // 워크스페이스 협업자로 등록
        User foundUser = findUser(foundWorkspace, userId);
        WorkspaceCollaborator foundWpCol = WpColRequestDto.toEntity(foundUser, foundWorkspace);
        wpColRepository.delete(foundWpCol);

        List<Board> foundAllBoards = findAllBoards(foundWorkspace);
        try {
            if (foundAllBoards != null && !foundAllBoards.isEmpty()) {
                for (Board foundBoard : foundAllBoards) {
                    // 해당 보드에 이미 등록된 협업자인 경우 예외 던지기
                    if (boardColRepository.existsByBoardColEmailAndBoard(foundUser.getEmail(), foundBoard)) {
                        throw new CustomException(CustomErrorCode.BOARD_COLLABORATOR_ALREADY_EXISTS);
                    }
                    BoardCollaborator boardCollaborator = BoardColRequestDto.toEntity(foundUser, foundBoard);
                    boardColRepository.delete(boardCollaborator);
                }
            }
        } catch(Exception e) {
            log.error("워크스페이스 협업자 추방에 실패했습니다. 이유: ", e.getMessage(), e);
            throw new CustomException(CustomErrorCode.WORKSPACE_COLLABORATOR_NOT_ACCESSIBLE);
        }

    }

    private Workspace findWorkspace(Long wpId) {

        return wpRepository.findById(wpId).orElseThrow(() ->
            new CustomException(CustomErrorCode.WORKSPACE_NOT_FOUND));
    }

    private User findUser(Workspace foundWorkspace, Long userId) {

        // 해당 워크스프이스에 이미 등록된 협업자인 경우 예외 던지기
        if (wpColRepository.existsByWorkspaceAndId(foundWorkspace, userId)) {
            throw new CustomException(CustomErrorCode.WORKSPACE_COLLABORATOR_ALREADY_EXISTS);
        } else {
            return userRepository.findById(userId).orElseThrow(() ->
                new CustomException(CustomErrorCode.USER_NOT_FOUND));
        }
    }

    private List<Board> findAllBoards(Workspace foundWorkspace) {
        return boardRepository.findAllByWorkspace(foundWorkspace).orElseThrow(() ->
            new CustomException(CustomErrorCode.BOARD_NOT_FOUND));
    }
}
