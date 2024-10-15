package com.sparta.spring_trello.domain.card.dto.response;

import com.sparta.spring_trello.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CardResponseDto {
    private Long id;
    private String title;
    private String contents;
    private LocalDate deadline;
//    private final List<Activity> activities;
//    private final List<Comment> comments;
}
