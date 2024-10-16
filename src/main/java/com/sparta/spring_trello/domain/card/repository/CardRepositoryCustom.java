package com.sparta.spring_trello.domain.card.repository;

import com.sparta.spring_trello.domain.card.dto.request.CardSearchDTO;
import com.sparta.spring_trello.domain.card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardRepositoryCustom {
    Page<Card> searchCards(CardSearchDTO criteria, Pageable pageable);
}
