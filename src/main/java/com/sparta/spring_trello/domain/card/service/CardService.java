package com.sparta.spring_trello.domain.card.service;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.card.dto.request.CardRequestDto;
import com.sparta.spring_trello.domain.card.dto.request.CardSearchDTO;
import com.sparta.spring_trello.domain.card.dto.response.CardDetailResponseDto;
import com.sparta.spring_trello.domain.card.dto.response.CardResponseDto;
import com.sparta.spring_trello.domain.card.entity.Activity;
import com.sparta.spring_trello.domain.card.entity.Card;
import com.sparta.spring_trello.domain.card.repository.ActivityRepository;
import com.sparta.spring_trello.domain.card.repository.CardRepository;
import com.sparta.spring_trello.domain.card.repository.CardRepositoryCustom;
import com.sparta.spring_trello.domain.comment.entity.Comment;
import com.sparta.spring_trello.domain.comment.repository.CommentRepository;
import com.sparta.spring_trello.domain.common.exception.CustomException;
import com.sparta.spring_trello.domain.common.exception.ErrorCode;
import com.sparta.spring_trello.domain.list.repository.ListsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final CardRepositoryCustom cardRepositoryCustom;
    private final ListsRepository listsRepository;

    // 카드 생성
    public CardResponseDto createCard(AuthUser authUser, CardRequestDto cardRequest) {

        checkPermission(); // 권한 확인

        // 리스트 ID로 리스트를 조회
        var list = listsRepository.findById(cardRequest.getListId())
                .orElseThrow(() -> new EntityNotFoundException("해당 리스트를 찾을 수 없습니다."));

        // 카드 생성 시 작성자 ID 설정
        Card card = Card.builder()
                .title(cardRequest.getTitle())
                .contents(cardRequest.getContents())
                .deadline(cardRequest.getDeadline())
                .userId(authUser.getUserId())
                .lists(list)
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
    public CardResponseDto updateCard(AuthUser authUser, Long cardId, CardRequestDto cardRequest) {
        // 카드 존재 여부 확인
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다."));

        // 카드 작성자인지 확인
        if (!card.getUserId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.NOT_CARD_AUTHOR);
        }

        checkPermission(); // 권한 확인

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
    public void deleteCard(AuthUser authUser, Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다."));

        // 카드 작성자인지 확인
        if (!card.getUserId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.NOT_CARD_AUTHOR);
        }

        checkPermission(); // 권한 확인

        // 카드 삭제
        cardRepository.delete(card);
    }

    // 카드 검색 기능
    public Page<Card> searchCards(CardSearchDTO criteria, Pageable pageable) {
        return cardRepositoryCustom.searchCards(criteria, pageable);
    }

    // 권한 확인 메서드
    private void checkPermission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isReadOnly = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("READONLY"));

        if (isReadOnly) {
            throw new CustomException(ErrorCode.READ_ONLY_USER_ERROR, "읽기 전용 사용자는 카드를 생성/삭제를 할 수 없습니다.");
        }
    }
}