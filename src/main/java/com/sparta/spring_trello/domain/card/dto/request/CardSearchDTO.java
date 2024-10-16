package com.sparta.spring_trello.domain.card.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CardSearchDTO {
    private String title;
    private String contents;
    private LocalDate deadline;
    private Long userId;
    private Long boardId;
}
