package com.sparta.spring_trello.domain.card.controller;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.card.dto.request.CardRequestDto;
import com.sparta.spring_trello.domain.card.dto.response.CardResponseDto;
import com.sparta.spring_trello.domain.card.service.CardService;
import com.sparta.spring_trello.domain.user.entity.User;
import com.sparta.spring_trello.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@RequestBody CardRequestDto cardRequest, AuthUser authUser) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        CardResponseDto createdCard = cardService.createCard(cardRequest, authUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }
}
