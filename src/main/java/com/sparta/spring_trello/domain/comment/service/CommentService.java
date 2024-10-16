package com.sparta.spring_trello.domain.comment.service;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.card.entity.Card;
import com.sparta.spring_trello.domain.card.repository.CardRepository;
import com.sparta.spring_trello.domain.comment.dto.request.CommentRequestDto;
import com.sparta.spring_trello.domain.comment.entity.Comment;
import com.sparta.spring_trello.domain.comment.repository.CommentRepository;
import com.sparta.spring_trello.domain.common.exception.CustomException;
import com.sparta.spring_trello.domain.common.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    // 댓글 작성
    public void createComment(Long cardId, CommentRequestDto commentRequest, AuthUser authUser) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다."));

        // 읽기 전용 권한 확인
        if (authUser.isReadOnly()) {
            throw new CustomException(ErrorCode.READ_ONLY_MEMBER);
        }

        // 댓글 생성
        Comment comment = Comment.builder()
                .text(commentRequest.getText())
                .emoji(commentRequest.getEmoji())
                .card(card)
                .userId(authUser.getUserId())  // 댓글 작성자 정보
                .build();

        commentRepository.save(comment);
    }

    // 댓글 수정
    public void updateComment(Long commentId, CommentRequestDto commentRequest, AuthUser authUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));

        // 댓글 작성자 확인
        if (!comment.getUserId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.NOT_COMMENT_AUTHOR);
        }

        // 새로운 댓글 객체 생성
        Comment updatedComment = Comment.builder()
                .id(comment.getId())  // 기존 ID 유지
                .text(commentRequest.getText())
                .emoji(commentRequest.getEmoji())
                .card(comment.getCard())
                .userId(comment.getUserId())
                .build();

        commentRepository.save(updatedComment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, AuthUser authUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));

        // 댓글 작성자 확인
        if (!comment.getUserId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.NOT_COMMENT_AUTHOR);
        }

        commentRepository.delete(comment);
    }
}
