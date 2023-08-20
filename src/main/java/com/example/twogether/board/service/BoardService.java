package com.example.twogether.board.service;

import com.example.twogether.board.dto.BoardRequestDto;
import com.example.twogether.board.dto.BoardResponseDto;
import com.example.twogether.board.entity.Board;
import com.example.twogether.board.repository.BoardRepository;
import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.user.entity.User;
import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.repository.WorkspaceRepository;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;

    // 보드 생성
    @Transactional
    public BoardResponseDto createBoard(User boardAuthor, Long workspaceId, BoardRequestDto boardRequestDto) {
        try {
            if (boardAuthor == null) {
                throw new CustomException(CustomErrorCode.LOGIN_REQUIRED);
            }

            Workspace foundWorkspace = findWorkspace(workspaceId);
            Board board = boardRequestDto.toEntity(foundWorkspace, boardAuthor);
            boardRepository.save(board);
            log.info("칸반 보드 생성에 성공했습니다.");
            return BoardResponseDto.of(board);
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
            if(boardRequestDto.getTitle()!=null) board.updateTitle(boardRequestDto);
            if(boardRequestDto.getColor()!=null) board.updateColor(boardRequestDto);
            if(boardRequestDto.getInfo()!=null) board.updateInfo(boardRequestDto);
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
        return workspaceRepository.findById(workspaceId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.WORKSPACE_NOT_FOUND));
    }

    private Board findByWorkspace_and_Board_Id(Workspace foundWorkspace, Long boardId) {

        return boardRepository.findByWorkspaceAndId(foundWorkspace, boardId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND));
    }
}
