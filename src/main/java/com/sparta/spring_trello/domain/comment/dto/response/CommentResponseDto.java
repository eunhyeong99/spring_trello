package com.sparta.spring_trello.domain.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private String text;
    private String emoji;
}
