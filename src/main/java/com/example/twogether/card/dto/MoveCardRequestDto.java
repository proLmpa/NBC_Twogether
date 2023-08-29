package com.example.twogether.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MoveCardRequestDto {
    private Long prevCardId;
    private Long nextCardId;
    private Long deckId;
}
