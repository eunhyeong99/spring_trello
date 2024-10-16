package com.sparta.spring_trello.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardRequestDto {

    private Long workspaceId;

    @NotBlank(message = "제목은 공백일 수 없습니다.")
    private String title;
}
