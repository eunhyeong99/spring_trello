package com.sparta.spring_trello.domain.comment.service;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.card.entity.Card;
import com.sparta.spring_trello.domain.card.repository.CardRepository;
import com.sparta.spring_trello.domain.comment.dto.request.CommentRequestDto;
import com.sparta.spring_trello.domain.comment.entity.Comment;
import com.sparta.spring_trello.domain.comment.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    // 댓글 작성
    @Transactional
    public void createComment(Long cardId, CommentRequestDto commentRequest, AuthUser authUser) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다."));

//        // 읽기 전용 권한 확인
//        if (authUser.isReadOnly()) {
//            throw new CustomException("Read-only members cannot create comments");
//        }

        // 댓글 생성
        Comment comment = Comment.builder()
                .text(commentRequest.getText())
                .emoji(commentRequest.getEmoji())
                .card(card)
                .userId(authUser.getUserId())  // 댓글 작성자 정보
                .build();

        commentRepository.save(comment);
    }
}
