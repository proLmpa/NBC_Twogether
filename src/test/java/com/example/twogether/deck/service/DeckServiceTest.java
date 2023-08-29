package com.example.twogether.deck.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.twogether.board.dto.BoardRequestDto;
import com.example.twogether.board.entity.Board;
import com.example.twogether.board.repository.BoardColRepository;
import com.example.twogether.board.repository.BoardRepository;
import com.example.twogether.board.service.BoardService;
import com.example.twogether.card.repository.CardLabelRepository;
import com.example.twogether.card.repository.CardRepository;
import com.example.twogether.checklist.repository.CheckListRepository;
import com.example.twogether.checklist.repository.ChlItemRepository;
import com.example.twogether.comment.repository.CommentRepository;
import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.deck.dto.DeckResponseDto;
import com.example.twogether.deck.dto.MoveDeckRequestDto;
import com.example.twogether.deck.entity.Deck;
import com.example.twogether.deck.repository.DeckRepository;
import com.example.twogether.user.dto.SignupRequestDto;
import com.example.twogether.user.entity.User;
import com.example.twogether.user.repository.UserRepository;
import com.example.twogether.user.service.UserService;
import com.example.twogether.workspace.dto.WpRequestDto;
import com.example.twogether.workspace.entity.Workspace;
import com.example.twogether.workspace.repository.WpRepository;
import com.example.twogether.workspace.service.WpService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeckServiceTest {
    @Autowired
    private DeckService deckService;
    @Autowired
    private DeckRepository deckRepository;
    @Autowired
    private BoardRepository boardRepository;
    private static final float CYCLE = 128f;
    Board board;
    @BeforeEach
    void setUp() {
        board = boardRepository.findById(1L).orElse(null);
    }

    @Test
    @DisplayName("덱 생성 테스트")
    void addTest() {

        String title1 = "test1";

        deckRepository.save(Deck.builder()
            .title(title1)
            .board(board)
            .build());
        List<Deck> decks = deckRepository.findAll();

        assertEquals(title1, decks.get(decks.size()-1).getTitle());
    }

    @Test
    @DisplayName("덱 단일 조회 테스트")
    void getTest() {
        List<Deck> decks = deckRepository.findAll();

        DeckResponseDto responseDto1 = deckService.getDeck(decks.get(0).getId());
        DeckResponseDto responseDto2 = deckService.getDeck(decks.get(1).getId());
        DeckResponseDto responseDto3 = deckService.getDeck(decks.get(2).getId());

        assertEquals("test1", responseDto1.getTitle());
        assertEquals("test2", responseDto2.getTitle());
        assertEquals("test3", responseDto3.getTitle());
    }

    @Test
    @DisplayName("덱 수정 테스트")
    void editTest() {
        List<Deck> decks = deckRepository.findAll();
        Deck target = decks.get(0);
        String title = target.getTitle();

        deckService.editDeck(target.getId(), "edited" + title);

        assertEquals("edited" + title, target.getTitle());
    }

    @Test
    @DisplayName("덱 이동 테스트 (1 > 0)")
    void moveTest1() {
        List<Deck> decks = deckRepository.findAll();
        Deck target = decks.get(1);
        Deck prev = null;
        Deck next = decks.get(0);
        MoveDeckRequestDto requestDto = new MoveDeckRequestDto(prev.getId(), next.getId());

        deckService.moveDeck(target.getId(), requestDto);

        assertEquals(next.getPosition() / 2f, target.getPosition());
    }

    @Test
    @DisplayName("덱 이동 테스트 (0 > 2)")
    void moveTest2() {
        List<Deck> decks = deckRepository.findAll();
        Deck target = decks.get(1);
        Deck prev = decks.get(2);
        Deck next = null;
        MoveDeckRequestDto requestDto = new MoveDeckRequestDto(prev.getId(), next.getId());

        deckService.moveDeck(target.getId(), requestDto);

        assertEquals(prev.getPosition() + CYCLE, target.getPosition());
    }

    @Test
    @DisplayName("덱 이동 테스트 (2 > 1)")
    void moveTest3() {
        List<Deck> decks = deckRepository.findAll();
        Deck target = decks.get(1);
        Deck prev = decks.get(0);
        Deck next = decks.get(2);
        MoveDeckRequestDto requestDto = new MoveDeckRequestDto(prev.getId(), next.getId());

        deckService.moveDeck(target.getId(), requestDto);

        assertEquals((prev.getPosition() + next.getPosition()) / 2f, target.getPosition());
    }

    @Test
    @DisplayName("덱 보관/복구 테스트")
    void archiveTest() {
        List<Deck> decks = deckRepository.findAll();
        Deck target = decks.get(0);
        boolean archived = target.isArchived();

        deckService.archiveDeck(target.getId());

        assertEquals(!archived, target.isArchived());
    }
}
//    @Test
//    @DisplayName("덱 삭제 테스트")
//    void deleteTest() {
//        List<Deck> decks = deckRepository.findAll();
//        Deck target = decks.get(0);
//        target.setArchived(true);
//
//        deckService.deleteDeck(target.getId());
//
//        assertNotEquals(target, decks.get(0));
//    }
//
//    @Test
//    @DisplayName("보관 안 된 덱 삭제 테스트")
//    void deleteFailTest() {
//        List<Deck> decks = deckRepository.findAll();
//        Deck target = decks.get(0);
//
//        try {
//            deckService.deleteDeck(target.getId());
//        } catch (CustomException e) {
//            assertEquals(CustomErrorCode.DECK_IS_NOT_ARCHIVE, e.getErrorCode());
//        }
//    }
