package com.sparta.spring_trello.domain.comment.controller;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.comment.dto.request.CommentRequestDto;
import com.sparta.spring_trello.domain.comment.service.CommentService;
import com.sparta.spring_trello.domain.user.entity.User;
import com.sparta.spring_trello.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards/{cardId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<Void> createComment(
            @PathVariable Long cardId,
            @RequestBody CommentRequestDto commentRequest,
            @AuthenticationPrincipal AuthUser authUser) {

        // 사용자 정보 확인
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 댓글 작성 서비스 호출
        commentService.createComment(cardId, commentRequest, authUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
