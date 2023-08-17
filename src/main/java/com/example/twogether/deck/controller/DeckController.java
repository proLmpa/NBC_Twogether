package com.example.twogether.deck.controller;

import com.example.twogether.common.dto.ApiResponseDto;
import com.example.twogether.deck.dto.DeckRequestDto;
import com.example.twogether.deck.dto.DeckResponseDto;
import com.example.twogether.deck.service.DeckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "덱 API", description = "덱 CRUD, 이동 기능과 관련된 API 정보를 담고 있습니다.")
public class DeckController {

    private final DeckService deckService;

    @Operation(summary = "덱 생성", description = "DeckRequestDto에 담긴 정보를 토대로 덱을 생성합니다.")
    @PostMapping("/decks")
    private ResponseEntity<ApiResponseDto> addDeck(@RequestBody DeckRequestDto requestDto) {
        deckService.addDeck(requestDto);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "덱 생성"));
    }

    @Operation(summary = "덱 단일 조회", description = "id와 일치하는 덱의 title과 덱의 id를 가지고 있는 카드들을 같이 반환합니다.")
    @GetMapping("/decks/{id}")
    private ResponseEntity<DeckResponseDto> getDeck(@PathVariable Long id) {
        DeckResponseDto responseDto = deckService.getDeck(id);
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "덱 title 수정", description = "id와 일치하는 덱의 title을 수정합니다.")
    @PutMapping("/decks/{id}/edit")
    private ResponseEntity<ApiResponseDto> editDeck(@PathVariable Long id, @RequestBody String title) {
        deckService.editDeck(id, title);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "덱 생성"));
    }

    // 덱 보관

    // 덱 복구

    // 덱 삭제
    @Operation(summary = "덱 삭제", description = "id와 일치하는 덱을 삭제합니다.")
    @DeleteMapping("/decks/{id}")
    private ResponseEntity<ApiResponseDto> deleteDeck(@PathVariable Long id) {
        deckService.deleteDeck(id);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "덱 삭제"));
    }

    // 덱 이동

}