package com.example.twogether.card.dto;

import com.example.twogether.card.entity.CardCollaborator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardColsResponseDto {

    private Long cardId;
    private String email;
    private String nickname;
    private List<CardCollaborator> cardCollaborators;

    public static CardColsResponseDto of(List<CardCollaborator> cardCollaborators) {

        return CardColsResponseDto.builder()
            .cardId(cardCollaborators.get(0).getCard().getId())
            .cardCollaborators(cardCollaborators)
            .build();
    }}
