package com.sparta.spring_trello.domain.card.service;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.card.dto.request.CardRequestDto;
import com.sparta.spring_trello.domain.card.dto.response.CardResponseDto;
import com.sparta.spring_trello.domain.card.entity.Card;
import com.sparta.spring_trello.domain.card.repository.CardRepository;
import com.sparta.spring_trello.exception.CustomException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    // 카드 생성
    public CardResponseDto createCard(CardRequestDto cardRequest, AuthUser authUser) {

        Card card = Card.builder()
                .title(cardRequest.getTitle())
                .contents(cardRequest.getContents())
                .deadline(cardRequest.getDeadline())
                .build();

        Card savedCard = cardRepository.save(card);
        return new CardResponseDto(
                savedCard.getId(),
                savedCard.getTitle(),
                savedCard.getContents(),
                savedCard.getDeadline());
    }

    // 카드 수정
    public CardResponseDto updateCard(Long cardId, CardRequestDto cardRequest, AuthUser authUser) {

        // 카드 존재 여부 확인
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다."));

        // 권한 확인 로직 (AuthUser가 수정 권한이 있는지 확인)
//        if (!authUser.hasPermissionToModifyCard(card)) {
//            throw new SecurityException("수정 권한이 없습니다.");
//        }

        // 카드 수정
        card.updateCard(
                cardRequest.getTitle(),
                cardRequest.getContents(),
                cardRequest.getDeadline()
        );

        Card updatedCard = cardRepository.save(card);

        return new CardResponseDto(
                updatedCard.getId(),
                updatedCard.getTitle(),
                updatedCard.getContents(),
                updatedCard.getDeadline()
        );
    }

    // 카드 상세 조회
    public CardResponseDto getCardDetails(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다."));

        return new CardResponseDto(
                card.getId(),
                card.getTitle(),
                card.getContents(),
                card.getDeadline()
//                card.getActivities(), // 활동 내역 추가
//                card.getComments()     // 댓글 추가
        );
    }

    // 카드 삭제
    @Transactional
    public void deleteCard(Long cardId, AuthUser authUser) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다."));

//        // 읽기 전용 권한 확인
//        if (authUser.isReadOnly()) {
//            throw new CustomException(,"Read-only members cannot delete cards");
//        }

        // 카드 삭제
        cardRepository.delete(card);
    }
}
