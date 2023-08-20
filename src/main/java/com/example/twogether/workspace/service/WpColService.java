package com.example.twogether.workspace.service;

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
    public void inviteWpCol(User wpAuthor, Long wpId, String email) {

        Workspace foundWorkspace = findWorkspace(wpId);

        // 워크스페이스를 생성한 사람만 협업자 초대 가능
        if (!foundWorkspace.getUser().getId().equals(wpAuthor.getId()) || wpAuthor.getRole()
            .equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(CustomErrorCode.NOT_YOUR_WORKSPACE);
        }

        // 워크스페이스 오너는 초대 불가
        if (email.equals(wpAuthor.getEmail())) {
            throw new CustomException(CustomErrorCode.THIS_IS_YOUR_WORKSPACE);
        }

        // 이미 등록된 사용자 초대 불가
        if (wpColRepository.existsByWorkspaceAndEmail(foundWorkspace, email)) {
            throw new CustomException(CustomErrorCode.WORKSPACE_COLLABORATOR_ALREADY_EXISTS);
        }

        // 워크스페이스 협업자로 등록
        User foundUser = findUser(email);
        WorkspaceCollaborator foundWpCol = WpColRequestDto.toEntity(foundUser, foundWorkspace);
        wpColRepository.save(foundWpCol);

        // 워크스페이스에 포함되어 있는 모든 보드에 협업자로 자동 등록
        List<Board> foundAllBoards = findAllBoards(foundWorkspace);

        if (foundAllBoards != null && !foundAllBoards.isEmpty()) {
            for (Board foundBoard : foundAllBoards) {
                // 해당 보드에 이미 등록된 협업자인 경우 예외 던지기
                if (boardColRepository.existsByUserEmailAndBoard(foundUser.getEmail(),
                    foundBoard)) {
                    throw new CustomException(CustomErrorCode.BOARD_COLLABORATOR_ALREADY_EXISTS);
                }
                BoardCollaborator boardCollaborator = BoardColRequestDto.toEntity(foundUser,
                    foundBoard);
                boardColRepository.save(boardCollaborator);
            }
        }
    }

    // 워크스페이스 협업자 추방
    @Transactional
    public void outWpCol(User wpAuthor, Long wpId, String email) {

        Workspace foundWorkspace = findWorkspace(wpId);

        // 워크스페이스를 생성한 사람만 협업자 추방하기 가능
        if (!foundWorkspace.getUser().getId().equals(wpAuthor.getId()) || wpAuthor.getRole()
            .equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(CustomErrorCode.NOT_YOUR_WORKSPACE);
        }

        // 워크스페이스 오너는 추방당하기 불가
        if (email.equals(wpAuthor.getEmail())) {
            throw new CustomException(CustomErrorCode.THIS_IS_YOUR_WORKSPACE);
        }

        // 워크스페이스 협업자로 등록
        WorkspaceCollaborator foundWpCol = findWpCol(foundWorkspace.getUser().getId());

        // 워크스페이스 협업자 삭제
        wpColRepository.delete(foundWpCol);

        // 워크스페이스에서 추방한 협업자 모든 하위 보드에서 자동 추방
        List<Board> foundAllBoards = findAllBoards(foundWorkspace);
        if (foundAllBoards != null && !foundAllBoards.isEmpty()) {
            for (Board foundBoard : foundAllBoards) {
                // 이미 추방된 보드 협업자
                if (!boardColRepository.existsByUserEmailAndBoard(email, foundBoard)) {
                    throw new CustomException(CustomErrorCode.BOARD_COLLABORATOR_ALREADY_OUT);
                }

                // 보드 협업자 삭제
                List<BoardCollaborator> boardCollaborators = boardColRepository.findByBoard(foundBoard);
                for (BoardCollaborator boardCollaborator : boardCollaborators) {
                    boardColRepository.delete(boardCollaborator);
                }
            }
        }
    }

    private Workspace findWorkspace(Long wpId) {

        return wpRepository.findById(wpId).orElseThrow(() ->
            new CustomException(CustomErrorCode.WORKSPACE_NOT_FOUND));
    }

    private User findUser(String email) {

        return userRepository.findByEmail(email).orElseThrow(() ->
                new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    private WorkspaceCollaborator findWpCol(Long wpColId) {

        return wpColRepository.findById(wpColId).orElseThrow(() ->
            new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    private List<Board> findAllBoards(Workspace foundWorkspace) {
        return boardRepository.findAllByWorkspace(foundWorkspace).orElseThrow(() ->
            new CustomException(CustomErrorCode.BOARD_NOT_FOUND));
    }
}
