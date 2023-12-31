package com.example.twogether.comment.service;

import com.example.twogether.alarm.dto.AlarmTargetRequestDto;
import com.example.twogether.alarm.entity.AlarmTarget;
import com.example.twogether.alarm.event.TriggerEventPublisher;
import com.example.twogether.alarm.repository.AlarmTargetRepository;
import com.example.twogether.board.entity.Board;
import com.example.twogether.board.entity.BoardCollaborator;
import com.example.twogether.board.repository.BoardColRepository;
import com.example.twogether.board.repository.BoardRepository;
import com.example.twogether.card.entity.Card;
import com.example.twogether.card.repository.CardRepository;
import com.example.twogether.comment.dto.CommentRequestDto;
import com.example.twogether.comment.dto.CommentResponseDto;
import com.example.twogether.comment.entity.Comment;
import com.example.twogether.comment.repository.CommentRepository;
import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.entity.UserRoleEnum;
import com.example.twogether.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final BoardRepository boardRepository;
    private final BoardColRepository boardColRepository;
    private final AlarmTargetRepository alarmTargetRepository;
    private final TriggerEventPublisher eventPublisher;

    @Transactional
    public void createComment(Long boardId, Long cardId, CommentRequestDto requestDto, User user) {

        User writer = findUser(user.getId());
        Board board = findBoard(boardId);
        Card card = findCard(cardId);

        checkBoardCreatorOrCollaborator(boardId, writer);

        Comment comment = requestDto.toEntity(writer, card);
        commentRepository.save(comment);

        // 보드 생성자와 협업자만 카드 댓글 알림 조회 가능
        List<BoardCollaborator> boardCols = findBoardCol(board);
        List<AlarmTarget> alarmTargets = new ArrayList<>();
        for (BoardCollaborator boardCol : boardCols) {
            if (boardCol.getEmail() != writer.getEmail()) {
                alarmTargets.add(AlarmTargetRequestDto.boardColToEntity(boardCol));
            }
        }
        if (board.getUser().getEmail() != writer.getEmail()) {
            alarmTargets.add(AlarmTargetRequestDto.userToEntity(board.getUser()));
        }
        alarmTargetRepository.saveAll(alarmTargets);

        eventPublisher.publishCardCommentEvent(writer, alarmTargets, card, comment);
    }

    @Transactional(readOnly = true)
    public CommentResponseDto getComment(Long commentId) {

        Comment comment = findComment(commentId);
        return CommentResponseDto.of(comment);
    }

    @Transactional
    public void editComment(Long commentId, CommentRequestDto requestDto, User user) {

        Comment comment = findComment(commentId);
        User editor = findUser(user.getId());

        confirmUser(editor, comment);
        comment.editContent(requestDto.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {

        Comment comment = findComment(commentId);
        User editor = findUser(user.getId());

        confirmUser(editor, comment);
        commentRepository.deleteById(comment.getId());
    }

    private Comment findComment(Long commentId) {

        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.COMMENT_NOT_FOUND));
    }

    private User findUser(Long userId) {

        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    private Board findBoard(Long boardId) {

        return boardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND));
    }

    private Card findCard(Long cardId) {

        return cardRepository.findById(cardId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.CARD_NOT_FOUND));
    }

    private List<BoardCollaborator> findBoardCol(Board board) {

        return boardColRepository.findAllByBoard(board);
    }

    // 생성자와 보드 협업자만 댓글 작성 가능하도록 로직 구현
    private void checkBoardCreatorOrCollaborator(Long boardId, User writer) {

        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.BOARD_NOT_FOUND));

        if (!board.getUser().getId().equals(writer.getId()) &&
            !boardColRepository.existsByEmail(writer.getEmail())) {
            throw new CustomException(CustomErrorCode.BOARD_COLLABORATOR_NOT_FOUND);
        }
    }

    private void confirmUser(User user, Comment comment) {

        if (!comment.getUser().getId().equals(user.getId()) && !user.getRole()
            .equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(CustomErrorCode.NOT_YOUR_COMMENT);
        }
    }
}
