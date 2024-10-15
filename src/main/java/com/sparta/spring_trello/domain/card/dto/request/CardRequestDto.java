package com.sparta.spring_trello.domain.card.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CardRequestDto {
    private Long id;
    private String title;
    private String contents;
    private LocalDate deadline;
}
