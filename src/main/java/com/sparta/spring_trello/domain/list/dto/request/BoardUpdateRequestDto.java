package com.sparta.spring_trello.domain.list.dto.request;

import lombok.Getter;

@Getter
public class BoardUpdateRequestDto {

    private String title;
    private String backgroundColor;
    private String image;
}
