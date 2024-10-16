package com.sparta.spring_trello.domain.comment.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class CommentResponseDto {
    private final Long id;
    private final String text;
    private final String emoji;
    private final Long userId;
    private final LocalTime createdAt;
    private final LocalTime updatedAt;
}
