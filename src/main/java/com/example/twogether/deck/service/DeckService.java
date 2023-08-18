package com.example.twogether.deck.service;

import com.example.twogether.deck.dto.DeckResponseDto;
import com.example.twogether.deck.dto.MoveDeckRequestDto;
import com.example.twogether.deck.entity.Deck;
import com.example.twogether.deck.repository.DeckRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;
    private final float cycle = 128f;

    public void addDeck(Long boardId, String title) {
        float max = findMaxPosition(boardId);
        Deck newDeck;
        if(max < 0) {
            newDeck = new Deck(title, cycle);
        } else {
            newDeck = new Deck(title, max + cycle);
        }
        deckRepository.save(newDeck);
    }

    @Transactional(readOnly = true)
    public DeckResponseDto getDeck(Long id) {
        Deck deck = findDeckById(id);
        return new DeckResponseDto(deck);
    }

    @Transactional
    public void editDeck(Long id, String title) {
        Deck deck = findDeckById(id);
        deck.editTitle(title);
    }

    public void deleteDeck(Long id) {
        Deck deck = findDeckById(id);
        if (deck.isDeleted()) {
            deckRepository.delete(deck);
        } else {
            throw new RuntimeException("덱이 deleted 상태일 때만 삭제 가능합니다.");
        }
    }

    @Transactional
    public void archiveDeck(Long id) {
        Deck deck = findDeckById(id);
        deck.archive();
    }

    @Transactional
    public void moveDeck(Long id, MoveDeckRequestDto requestDto) {
        Deck deck = findDeckById(id);

        Deck prev = deckRepository.findById(requestDto.getPrevDeckId()).orElse(null);
        Deck next = deckRepository.findById(requestDto.getNextDeckId()).orElse(null);

        if (prev != null && next != null) { // 두 덱 사이로 옮길 때
            deck.editPosition((prev.getPosition() + next.getPosition()) / 2f);
        } else if (prev == null) { // 맨 처음으로 옮길 때
            deck.editPosition(next.getPosition() / 2f);
        } else { // 맨 마지막으로 옮길 때
            deck.editPosition(prev.getPosition() + cycle);
        }
    }

    private Deck findDeckById(Long id) {
        return deckRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    private float findMaxPosition(Long boardId) {
        float max = -1;
        List<Deck> decks = deckRepository.findAllByBoard_Id(boardId);
        if (decks.isEmpty()) {
            return max;
        } else {
            for(Deck deck : decks)
                Math.max(max, deck.getPosition());
            return max;
        }
    }
}
