package com.sparta.spring_trello.domain.board.dto.response;

import com.sparta.spring_trello.domain.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardSimpleResponseDto {
    private final Long id;
    private final String title;

    public BoardSimpleResponseDto(Long id, String title) {
        this.id = id;
        this.title = title;

    }

    public static BoardSimpleResponseDto from(Board board) {
        return new BoardSimpleResponseDto(
                board.getId(),
                board.getTitle());
    }
}
