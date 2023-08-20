package com.example.twogether.workspace.service;

import com.example.twogether.board.entity.BoardMember;
import com.example.twogether.board.repository.BoardMemberRepository;
import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.entity.UserRoleEnum;
import com.example.twogether.user.repository.UserRepository;
import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.entity.WorkspaceMember;
import com.example.twogether.workspace.repository.WorkspaceMemberRepository;
import com.example.twogether.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceMemberService {
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final BoardMemberRepository boardMemberRepository;
    private final UserRepository userRepository;

    // 워크스페이스 작업자 초대
    @Transactional
    public void createWorkCollabo(User user, Long workspaceId, Long boardId, Long userId) {
        Workspace workspace = findWorkspace(user, workspaceId);
        if (workspace.getUser().getId().equals(user.getId()) || user.getRole()
            .equals(UserRoleEnum.ADMIN)) {
            WorkspaceMember workCollabo = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, userId);//.orElseThrow(() -> new CustomException(CustomErrorCode.WORKSPACE_MEMBER_NOT_FOUND));
            BoardMember boardCollabo = boardMemberRepository.findByBoardIdAndBoardCollaboId(boardId, userId);//.orElseThrow(() -> new CustomException(CustomErrorCode.BOARD_MEMBER_NOT_FOUND));

            try {
                workspaceMemberRepository.save(workCollabo);
                boardMemberRepository.save(boardCollabo);
            } catch (Exception e) {
                throw new RuntimeException("워크스페이스 협업자 초대에 실패했습니다. 이유: " + e.getMessage(), e);
            }
        }
    }
    // 워크스페이스 작업자 추방
    @Transactional
    public void outWorkCollabo(User user,Long workspaceId, Long boardId, Long userId) {
        Workspace workspace = findWorkspace(user, workspaceId);
        if (workspace.getUser().getId().equals(user.getId()) || user.getRole()
            .equals(UserRoleEnum.ADMIN)) {
            WorkspaceMember workCollabo = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, userId);
            BoardMember boardCollabo = boardMemberRepository.findByBoardIdAndBoardCollaboId(boardId, userId);

            try {
                workspaceMemberRepository.delete(workCollabo);
                boardMemberRepository.delete(boardCollabo);
            } catch (Exception e) {
                throw new RuntimeException("워크스페이스 협업자 삭제에 실패했습니다. 이유: " + e.getMessage(), e);
            }
        } else throw new CustomException(CustomErrorCode.WORKSPACE_NOT_USER);
    }

    private Workspace findWorkspace(User user, Long workspaceId) {
        return workspaceRepository.findById(workspaceId).orElseThrow(() ->
            new CustomException(CustomErrorCode.WORKSPACE_NOT_FOUND));
    }
}

