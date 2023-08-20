package com.example.twogether.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardCollaboratorService {

/*    private final BoardCollaboratorRepository boardCollaboratorRepository;
    private final BoardRepository boardRepository;

    // 보드 협업자 초대 - 허락받아야 초대되는 로직으로 develop 할지 고민 중
    @Transactional
    public void addBoardCollaborator(Long boardId, Long boardCollaboratorId) {

        try {
            Board foundBoard = findBoard(boardId);
            BoardCollaborator foundBoardCollaborator = findByBoard_and_BoardCollaborator_Id(foundBoard, boardCollaboratorId);

            BoardCollaborator boardCollaborator = BoardCollaboratorRequestDto.toEntity(collaborator, board);
            boardCollaboratorRepository.save(boardCollaborator);
        } catch (Exception e) {
            throw new RuntimeException("협업자 등록에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 협업 초대받은 보드 협업자 명단 수정
    @Transactional
    public void updateCollaborator(Board board, BoardCollaborator boardCollaborator, User newCollaborator) {
        try {
            if (!boardCollaborator.getBoard().equals(board)) {
                throw new IllegalArgumentException("해당 칸반 보드의 협업자가 아닙니다.");
            }
            //명단에 내가 이미 있어서 또 초대할 필요가 없는지 확인
            if (boardCollaborator.getBoard().getBoardMembers().stream()
                .anyMatch(user -> user.getCollaborator().equals(newCollaborator))) {
                throw new DuplicateRequestException("이미 협업자로 할당된 사용자입니다.");
            }
            boardCollaborator.updateCollaborator(newCollaborator);
        } catch (Exception e) {
            throw new RuntimeException("협업자 수정에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 협업 초대받은 보드 협업자 삭제
    @Transactional
    public void deleteCollaborator(Long boardId, Long userId) {
        BoardCollaborator boardCollaborator = boardCollaboratorRepository.findByBoard_IdAndCollaborator_Id(boardId, userId)
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
            List<BoardCollaborator> boardCollaborators = boardCollaboratorRepository.findByBoardCollaborator(
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
        List<BoardCollaborator> boardList = boardCollaboratorRepository.findByBoard_Id(id);

        return BoardMembersResponseDto.of(boardList);
    }

    private BoardCollaborator findCollaborator(Long boardMemberId) {
        return boardCollaboratorRepository.findById(boardMemberId)
            .orElseThrow(() -> new NotFoundException("해당 칸반 보드에 존재하지 않는 협업자입니다."));
    }

    private Board findBoard(Long boardId) {
        // 존재하지 않는 보드를 찾는 경우 예외 던지기
        return boardRepository.findById(boardId).orElseThrow(() -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND));
    }

    private BoardCollaborator findByBoard_and_BoardCollaborator_Id(Board foundBoard, Long boardCollaboratorId) {

        // 해당 보드에 이미 등록된 협업자인 경우 예외 던지기
        if(boardCollaboratorRepository.existsByBoardAndBoardCollaborator_Id(foundBoard, boardCollaboratorId)) {
            throw new CustomException(CustomErrorCode.BOARD_MEMBER_ALREADY_EXISTS);
        }
        else {
            return boardCollaboratorRepository.findByBoardANDBoardCollaborator_Id(foundBoard, boardCollaboratorId);
        }
    }*/
}
