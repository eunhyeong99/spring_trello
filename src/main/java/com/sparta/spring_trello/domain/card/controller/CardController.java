package com.sparta.spring_trello.domain.card.controller;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.card.dto.request.CardRequestDto;
import com.sparta.spring_trello.domain.card.dto.response.CardDetailResponseDto;
import com.sparta.spring_trello.domain.card.entity.Card;
import com.sparta.spring_trello.domain.card.service.CardService;
import com.sparta.spring_trello.domain.user.repository.UserRepository;
import com.sparta.spring_trello.util.ApiResponse;
import com.sparta.spring_trello.domain.card.dto.request.CardSearchDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCard(
            @RequestBody CardRequestDto cardRequest,
            @AuthenticationPrincipal AuthUser authUser) {
        cardService.createCard(cardRequest, authUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("카드가 작성되었습니다."));
    }

    // 카드 수정
    @PutMapping("/{cardId}")
    public ResponseEntity<ApiResponse<?>> updateCard(
            @PathVariable Long cardId,
            @RequestBody CardRequestDto cardRequest,
            @AuthenticationPrincipal AuthUser authUser) {
        cardService.updateCard(cardId, cardRequest, authUser);
        return ResponseEntity.ok(ApiResponse.success("카드가 수정되었습니다."));
    }

    // 카드 상세 조회 (활동 내역 및 댓글 포함)
    @GetMapping("/{cardId}")
    public ResponseEntity<ApiResponse<CardDetailResponseDto>> getCardDetails(@PathVariable Long cardId) {
        CardDetailResponseDto cardDetails = cardService.getCardDetails(cardId);
        return ResponseEntity.ok(ApiResponse.success(cardDetails));
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<ApiResponse<?>> deleteCard(
            @PathVariable Long cardId,
            @AuthenticationPrincipal AuthUser authUser) {
        cardService.deleteCard(cardId, authUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("카드가 삭제되었습니다."));
    }

    //카드 검색
    @GetMapping("/search")
    public ResponseEntity<Page<Card>> searchCards(@ModelAttribute CardSearchDTO criteria, Pageable pageable) {
        Page<Card> cards = cardService.searchCards(criteria, pageable);
        return ResponseEntity.ok(cards);
    }
}
