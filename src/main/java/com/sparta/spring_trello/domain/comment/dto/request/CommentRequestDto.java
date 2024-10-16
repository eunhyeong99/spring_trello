package com.sparta.spring_trello.domain.comment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private String text;
    private String emoji;
}
