package com.sparta.spring_trello.domain.card.service;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.card.dto.request.CardRequestDto;
import com.sparta.spring_trello.domain.card.dto.response.CardDetailResponseDto;
import com.sparta.spring_trello.domain.card.dto.response.CardResponseDto;
import com.sparta.spring_trello.domain.card.entity.Activity;
import com.sparta.spring_trello.domain.card.entity.Card;
import com.sparta.spring_trello.domain.card.repository.ActivityRepository;
import com.sparta.spring_trello.domain.card.repository.CardRepository;
import com.sparta.spring_trello.domain.comment.entity.Comment;
import com.sparta.spring_trello.domain.comment.repository.CommentRepository;
import com.sparta.spring_trello.domain.common.exception.CustomException;
import com.sparta.spring_trello.domain.common.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ActivityRepository activityRepository;
    private final CommentRepository commentRepository;

    // 카드 생성
    public CardResponseDto createCard(CardRequestDto cardRequest, AuthUser authUser) {

        // 읽기 전용 권한 확인
        if (authUser.isReadOnly()) {
            throw new CustomException(ErrorCode.READ_ONLY_MEMBER);
        }

        // 카드 생성 시 작성자 ID 설정
        Card card = Card.builder()
                .title(cardRequest.getTitle())
                .contents(cardRequest.getContents())
                .deadline(cardRequest.getDeadline())
                .userId(authUser.getUserId())
                .build();

        Card savedCard = cardRepository.save(card);

        // 활동 내역 추가 (카드 생성)
        Activity activity = new Activity(savedCard, "카드 생성", "카드가 생성되었습니다.");
        activityRepository.save(activity);

        return new CardResponseDto(
                savedCard.getId(),
                savedCard.getTitle(),
                savedCard.getContents(),
                savedCard.getDeadline(),
                List.of(activity),
                List.of()
        );
    }

    // 카드 수정
    public CardResponseDto updateCard(Long cardId, CardRequestDto cardRequest, AuthUser authUser) {
        // 카드 존재 여부 확인
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다."));

        // 카드 작성자인지 확인
        if (!card.getUserId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.NOT_CARD_AUTHOR);
        }

        // 읽기 전용 권한 확인
        if (authUser.isReadOnly()) {
            throw new CustomException(ErrorCode.READ_ONLY_MEMBER);
        }

        // 카드 수정
        card.updateCard(
                cardRequest.getTitle(),
                cardRequest.getContents(),
                cardRequest.getDeadline()
        );

        Card updatedCard = cardRepository.save(card);

        // 활동 내역 추가 (카드 수정)
        Activity activity = new Activity(updatedCard, "카드 수정", "카드가 수정되었습니다.");
        activityRepository.save(activity);

        // 카드에 해당하는 활동 내역 조회
        List<Activity> activities = activityRepository.findByCardId(cardId);

        // 카드에 해당하는 댓글 조회
        List<Comment> comments = commentRepository.findByCardId(cardId);

        return new CardResponseDto(
                updatedCard.getId(),
                updatedCard.getTitle(),
                updatedCard.getContents(),
                updatedCard.getDeadline(),
                activities,
                comments
        );
    }

    // 카드 상세 조회
    public CardDetailResponseDto getCardDetails(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다."));

        // 카드에 해당하는 활동 내역 조회
        List<Activity> activities = activityRepository.findByCardId(cardId);

        // 카드에 해당하는 댓글 조회
        List<Comment> comments = commentRepository.findByCardId(cardId);

        return CardDetailResponseDto.builder()
                .id(card.getId())
                .title(card.getTitle())
                .contents(card.getContents())
                .deadline(card.getDeadline())
                .activities(activities)
                .comments(comments)
                .build();
    }

    // 카드 삭제
    public void deleteCard(Long cardId, AuthUser authUser) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다."));

        // 카드 작성자인지 확인
        if (!card.getUserId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.NOT_CARD_AUTHOR);
        }

        // 읽기 전용 권한 확인
        if (authUser.isReadOnly()) {
            throw new CustomException(ErrorCode.READ_ONLY_MEMBER);
        }

        // 카드 삭제
        cardRepository.delete(card);

        // 활동 내역 추가 (카드 삭제)
        Activity activity = new Activity(card, "카드 삭제", "카드가 삭제되었습니다.");
        activityRepository.save(activity);
    }
}