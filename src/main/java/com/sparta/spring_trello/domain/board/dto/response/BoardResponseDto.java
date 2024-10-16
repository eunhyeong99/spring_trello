package com.sparta.spring_trello.domain.board.dto.response;

import com.sparta.spring_trello.domain.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {

    private final Long id;
    private final String title;
    private final String backgroundColor;
    private final String image;

    private BoardResponseDto(Long id, String title, String backgroundColor, String image) {
        this.id = id;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.image = image;
    }

    public static BoardResponseDto from(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getBackgroundColor(),
                board.getImage());
    }

}
