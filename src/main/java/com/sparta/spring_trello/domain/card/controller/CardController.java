package com.sparta.spring_trello.domain.card.controller;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.card.dto.request.CardRequestDto;
import com.sparta.spring_trello.domain.card.dto.request.CardSearchDTO;
import com.sparta.spring_trello.domain.card.dto.response.CardDetailResponseDto;
import com.sparta.spring_trello.domain.card.entity.Card;
import com.sparta.spring_trello.domain.card.service.CardService;
import com.sparta.spring_trello.util.ApiResponse;
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

    /**
     * 카드 생성
     * @param authUser 로그인한 유저(=관리자)
     * @param cardRequest 카드 생성에 필요한 정보
     * @return cardResponse 생성된 카드의 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCard(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CardRequestDto cardRequest) {
        cardService.createCard(authUser, cardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("카드가 작성되었습니다."));
    }

    /**
     * 카드 수정
     * @param authUser 로그인한 유저(=관리자)
     * @param cardId 수정할 카드 ID
     * @param cardRequest 카드 생성에 필요한 정보
     * @return cardResponse 수정된 카드의 정보
     */
    @PutMapping("/{cardId}")
    public ResponseEntity<ApiResponse<?>> updateCard(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable(name ="cardId") Long cardId,
            @RequestBody CardRequestDto cardRequest) {
        cardService.updateCard(authUser, cardId, cardRequest);
        return ResponseEntity.ok(ApiResponse.success("카드가 수정되었습니다."));
    }

    /**
     * 카드 상세 조회
     * @param cardId 조회할 카드 ID
     */
    @GetMapping("/{cardId}")
    public ResponseEntity<ApiResponse<CardDetailResponseDto>> getCardDetails(@PathVariable("cardId")Long cardId) {
        CardDetailResponseDto cardDetails = cardService.getCardDetails(cardId);
        return ResponseEntity.ok(ApiResponse.success(cardDetails));
    }

    /**
     * 카드 삭제
     * @param authUser 로그인한 유저(=관리자)
     * @param cardId 삭제할 카드 ID
     * @return cardResponse 삭제한 카드의 정보
     */
    @DeleteMapping("/{cardId}")
    public ResponseEntity<ApiResponse<?>> deleteCard(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("cardId") Long cardId) {
        cardService.deleteCard(authUser, cardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("카드가 삭제되었습니다."));
    }

    // 카드 검색
    @GetMapping("/search")
    public ResponseEntity<Page<Card>> searchCards(@ModelAttribute CardSearchDTO criteria, Pageable pageable) {
        Page<Card> cards = cardService.searchCards(criteria, pageable);
        return ResponseEntity.ok(cards);
    }
}
