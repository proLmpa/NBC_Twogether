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
import com.example.twogether.workspace.dto.WpResponseDto;
import com.example.twogether.workspace.dto.WpsResponseDto;
import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.entity.WorkspaceCollaborator;
import com.example.twogether.workspace.entity.WpColWp;
import com.example.twogether.workspace.entity.WpColWpId;
import com.example.twogether.workspace.repository.WpColRepository;
import com.example.twogether.workspace.repository.WpColWpRepository;
import com.example.twogether.workspace.repository.WpRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final WpColWpRepository wpColWpRepository;
    private final BoardColRepository boardColRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 워크스페이스 협업자 초대 - 보드 협업자로도 자동 초대
    @Transactional
    public void inviteWpCol(User user, Long wpId, String email) {

        Workspace foundWorkspace = findWpById(wpId); // 중복되는 코드 처리 고민 중

        // 워크스페이스를 생성한 사람만 협업자 초대 가능
        if (!foundWorkspace.getUser().getId().equals(user.getId()) &&
            !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(CustomErrorCode.NOT_YOUR_WORKSPACE);
        }

        // 워크스페이스 오너는 초대당하기 불가 - 해당 사항에 대해 추후 프론트에서 예외처리되면 삭제될 예정
        if (email.equals(user.getEmail())) {
            throw new CustomException(CustomErrorCode.THIS_IS_YOUR_WORKSPACE);
        }

        // 이미 등록된 사용자 초대당하기 불가
        if (wpColRepository.existsByWorkspaceAndEmail(foundWorkspace, email)) {
            throw new CustomException(CustomErrorCode.WORKSPACE_COLLABORATOR_ALREADY_EXISTS);
        }

        // 워크스페이스 협업자로 등록
        User foundUser = findUser(email);
        WorkspaceCollaborator newWpCol = WpColRequestDto.toEntity(foundUser, foundWorkspace);

        // 아이디 수동 할당 - 데이터가 덮어 씌어지는 문제 방지
        newWpCol.assignNewId();
        wpColRepository.save(newWpCol);

        // 워크스페이스에서 초대한 협업자 모든 하위 보드도 자동 초대
        List<Board> foundAllBoards = findAllBoards(foundWorkspace);
        if (foundAllBoards != null && !foundAllBoards.isEmpty()) {

            for (Board foundBoard : foundAllBoards) {

                // 해당 보드에 이미 등록된 협업자인 경우 예외 던지기
                if (!boardColRepository.existsByBoardAndEmail(foundBoard, foundUser.getEmail())) {
                    BoardCollaborator boardCollaborator = BoardColRequestDto.toEntity(foundUser, foundBoard);
                    boardColRepository.save(boardCollaborator);
                }
            }
        }
    }

    // 워크스페이스 협업자 추방
    @Transactional
    public void outWpCol(User user, Long wpId, String email) {

        Workspace foundWorkspace = findWpById(wpId);

        // 워크스페이스를 생성한 사람만 협업자 추방하기 가능
        if (!foundWorkspace.getUser().getId().equals(user.getId()) &&
            !user.getRole().equals(UserRoleEnum.ADMIN)) {

            log.error("워크스페이스를 생성한 사람만 협업자 추방할 수 있습니다.");
            throw new CustomException(CustomErrorCode.NOT_YOUR_WORKSPACE);
        }

        // 워크스페이스 오너는 추방당하기 불가 - 해당 사항에 대해 추후 프론트에서 예외처리되면 삭제될 예정
        if (email.equals(user.getEmail())) {
            log.error("워크스페이스의 오너는 초대할 수 없습니다.");
            throw new CustomException(CustomErrorCode.THIS_IS_YOUR_WORKSPACE);
        }

        User foundUser = findUser(email);

        // 워크스페이스 협업자 삭제
        WorkspaceCollaborator foundWpCol = findWpColByEmail(foundWorkspace, email);
        wpColRepository.delete(foundWpCol);

        // 워크스페이스에서 추방한 협업자 모든 하위 보드에서 자동 추방
        List<Board> foundAllBoards = findAllBoards(foundWorkspace);
        for (Board foundBoard : foundAllBoards) {

            List<BoardCollaborator> boardCollaborators = boardColRepository.findByBoard(foundBoard);

            if (boardCollaborators != null && !boardCollaborators.isEmpty()) {
                // 이미 추방된 보드 협업자
                if (!boardColRepository.existsByBoardAndEmail(foundBoard, email)) {
                    throw new CustomException(CustomErrorCode.BOARD_COLLABORATOR_ALREADY_OUT);
                }

                // 보드 협업자 삭제
                BoardCollaborator foundBoardCol = findBoardCol(foundBoard, foundUser);
                boardColRepository.delete(foundBoardCol);
            }
        }
    }

    // 초대된 워크스페이스 단건 조회
    @Transactional(readOnly = true)
    public WpResponseDto getWpCol(User user, Long wpId) {

        Workspace foundWorkspace = findInvitedWp(user.getEmail(), wpId);
        return WpResponseDto.of(foundWorkspace);
    }

    // 초대된 워크스페이스 전체 조회 - 리팩토링 필요
    @Transactional(readOnly = true)
    public WpsResponseDto getWpCols(User user) {

        List<Workspace> foundWorkspaces = findAllWpsByEmail(user.getEmail());
        return WpsResponseDto.of(foundWorkspaces);
    }

    private Workspace findWpById(Long wpId) {

        return wpRepository.findById(wpId).orElseThrow(() ->
            new CustomException(CustomErrorCode.WORKSPACE_NOT_FOUND));
    }

    private Workspace findInvitedWp(String email, Long wpId) {

        return wpRepository.findByIdAndWorkspaceCollaborators_Email(wpId, email).orElseThrow(() ->
            new CustomException(CustomErrorCode.WORKSPACE_NOT_FOUND));
    }

    private List<Workspace> findAllWpsByEmail(String email) {
        return wpRepository.findAllByWorkspaceCollaborators_Email(email);
    }

    private User findUser(String email) {

        return userRepository.findByEmail(email).orElseThrow(() ->
            new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    private List<Board> findAllBoards(Workspace foundWorkspace) {
        return boardRepository.findAllByWorkspace(foundWorkspace).orElseThrow(() ->
            new CustomException(CustomErrorCode.BOARD_NOT_FOUND));
    }

    private WorkspaceCollaborator findWpColByEmail(Workspace foundWorkspace, String email) {

        return wpColRepository.findByWorkspaceAndEmail(foundWorkspace, email).orElseThrow(() ->
            new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    private BoardCollaborator findBoardCol(Board foundBoard, User foundUser) {

        return boardColRepository.findByBoardAndEmail(foundBoard, foundUser.getEmail())
            .orElseThrow(() ->
                new CustomException(CustomErrorCode.BOARD_COLLABORATOR_NOT_FOUND));
    }

    private List<WorkspaceCollaborator> findUserCollaborators(User user) {
        return wpColRepository.findByUser(user);
    }

    private List<BoardCollaborator> findUserBoardCollaborators(User user) {
        return boardColRepository.findByUser(user);
    }
}
