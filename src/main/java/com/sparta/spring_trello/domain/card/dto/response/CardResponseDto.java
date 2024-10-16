package com.sparta.spring_trello.domain.card.dto.response;

import com.sparta.spring_trello.domain.card.entity.Activity;
import com.sparta.spring_trello.domain.card.entity.Card;
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
    private final List<Activity> activities;
    private final List<Comment> comments;


    public CardResponseDto(Card card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.contents = card.getContents();
        this.deadline = card.getDeadline();
        this.activities = card.getActivities() != null ? List.copyOf(card.getActivities()) : List.of();
        this.comments = card.getComments() != null ? List.copyOf(card.getComments()) : List.of();

    }
}
