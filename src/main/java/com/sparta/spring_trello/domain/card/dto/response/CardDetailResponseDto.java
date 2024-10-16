package com.sparta.spring_trello.domain.card.dto.response;

import com.sparta.spring_trello.domain.card.entity.Activity;
import com.sparta.spring_trello.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CardDetailResponseDto {
    private Long id;
    private String title;
    private String contents;
    private LocalDate deadline;
    private final List<Activity> activities;
    private final List<Comment> comments;
}
