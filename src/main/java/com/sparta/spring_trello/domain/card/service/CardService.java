package com.sparta.spring_trello.domain.card.service;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.card.dto.request.CardRequestDto;
import com.sparta.spring_trello.domain.card.dto.response.CardResponseDto;
import com.sparta.spring_trello.domain.card.entity.Card;
import com.sparta.spring_trello.domain.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return new CardResponseDto(savedCard.getId(), savedCard.getTitle(), savedCard.getContents(), savedCard.getDeadline());
    }
}
