package com.sparta.spring_trello.domain.comment.controller;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.comment.dto.request.CommentRequestDto;
import com.sparta.spring_trello.domain.comment.service.CommentService;
import com.sparta.spring_trello.util.ApiResponse;
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

    // 댓글 작성
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createComment(
            @PathVariable Long cardId,
            @RequestBody CommentRequestDto commentRequest,
            @AuthenticationPrincipal AuthUser authUser) {
        commentService.createComment(cardId, commentRequest, authUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("댓글이 작성되었습니다."));
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<?>> updateComment(
            @PathVariable Long commentId,
            @PathVariable Long cardId,
            @RequestBody CommentRequestDto commentRequest,
            @AuthenticationPrincipal AuthUser authUser) {
        commentService.updateComment(commentId, commentRequest, authUser);
        return ResponseEntity.ok(ApiResponse.success("댓글이 수정되었습니다."));
    }
}