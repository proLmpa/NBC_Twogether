package com.example.twogether.deck.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MoveDeckRequestDto {
    private Long prevDeckId;
    private Long nextDeckId;
}