package com.example.twogether.board.service;

import com.example.twogether.board.dto.BoardColRequestDto;
import com.example.twogether.board.dto.BoardRequestDto;
import com.example.twogether.board.dto.BoardResponseDto;
import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.board.repository.BoardColRepository;
import com.example.twogether.board.repository.BoardRepository;
import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.repository.UserRepository;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardColRepository boardColRepository;
    private final WpRepository wpRepository;


    // 보드 생성
    @Transactional
    public void createBoard(User boardAuthor, Long workspaceId, BoardRequestDto boardRequestDto) {
        try {
            if (boardAuthor == null) {
                throw new CustomException(CustomErrorCode.LOGIN_REQUIRED);
            }

            Workspace foundWorkspace = findWorkspace(workspaceId);
            Board foundBoard = boardRequestDto.toEntity(foundWorkspace, boardAuthor);
            boardRepository.save(foundBoard);
            log.info("칸반 보드 생성에 성공했습니다.");

            // 보드 협업자 자동 등록
            List<WorkspaceCollaborator> workspaceCollaborators = foundWorkspace.getWorkspaceCollaborators();
            for (WorkspaceCollaborator workspaceCollaborator : workspaceCollaborators) {
                if (!boardColRepository.existsByUserEmailAndBoard(workspaceCollaborator.getUser().getEmail(), foundBoard)) {
                    BoardCollaborator newBoardCollaborator = BoardCollaborator.builder()
                        .user(workspaceCollaborator.getUser())
                        .board(foundBoard)
                        .build();
                    boardColRepository.save(newBoardCollaborator);
                }
            }
        } catch (Exception e) {
            log.error("칸반 보드 생성에 실패했습니다. 이유: ", e.getMessage(), e);
            throw new CustomException(CustomErrorCode.BOARD_NOT_ACCESSIBLE);
        }
    }

    // 보드 단건 조회
    @Transactional(readOnly = true)
    public BoardResponseDto getBoardById(User boardAuthor, Long workspaceId, Long boardId) {
        try {
            if (boardAuthor == null) {
                throw new CustomException(CustomErrorCode.LOGIN_REQUIRED);
            }

            Workspace foundWorkspace = findWorkspace(workspaceId);
            Board board = findByWorkspace_and_Board_Id(foundWorkspace, boardId);
            return BoardResponseDto.of(board);
        } catch (Exception e) {
            log.error("칸반 보드 단건 조회를 실패했습니다. 이유: ", e.getMessage(), e);
            throw new CustomException(CustomErrorCode.BOARD_NOT_ACCESSIBLE);
        }
    }

    // 보드 수정
    @Transactional
    public Board editBoard(User boardAuthor, Long workspaceId, Long boardId, BoardRequestDto boardRequestDto) {
        try {
            if (boardAuthor == null) {
                throw new CustomException(CustomErrorCode.LOGIN_REQUIRED);
            }

            Workspace foundWorkspace = findWorkspace(workspaceId);
            Board board = findByWorkspace_and_Board_Id(foundWorkspace, boardId);
            if(boardRequestDto.getTitle()!=null) board.editTitle(boardRequestDto);
            if(boardRequestDto.getColor()!=null) board.editColor(boardRequestDto);
            if(boardRequestDto.getInfo()!=null) board.editInfo(boardRequestDto);
            return board;
        } catch (Exception e) {
            log.error("칸반 보드 수정에 실패했습니다. 이유: ", e.getMessage(), e);
            throw new CustomException(CustomErrorCode.BOARD_NOT_ACCESSIBLE);
        }
    }

    // 보드 삭제
    @Transactional
    public void deleteBoard(User boardAuthor, Long workspaceId, Long boardId) {
        try {
            if (boardAuthor == null) {
                throw new CustomException(CustomErrorCode.LOGIN_REQUIRED);
            }

            Workspace foundWorkspace = findWorkspace(workspaceId);
            Board board = findByWorkspace_and_Board_Id(foundWorkspace, boardId);

            if (!board.getBoardAuthor().getEmail().equals(boardAuthor.getEmail())) {
                throw new CustomException(CustomErrorCode.NOT_YOUR_BOARD);
            }
            boardRepository.delete(board);
        } catch (Exception e) {
            log.error("칸반 보드 삭제에 실패했습니다. 이유: ", e.getMessage(), e);
            throw new CustomException(CustomErrorCode.BOARD_NOT_ACCESSIBLE);
        }
    }

    private Workspace findWorkspace(Long workspaceId) {
        return wpRepository.findById(workspaceId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.WORKSPACE_NOT_FOUND));
    }

    private Board findByWorkspace_and_Board_Id(Workspace foundWorkspace, Long boardId) {

        return boardRepository.findByWorkspaceAndId(foundWorkspace, boardId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND));
    }
}
