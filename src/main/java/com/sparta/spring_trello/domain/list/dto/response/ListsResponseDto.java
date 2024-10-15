package com.sparta.spring_trello.domain.list.dto.response;

import com.sparta.spring_trello.domain.list.entity.Lists;
import lombok.Getter;

@Getter
public class ListsResponseDto {
    private final Long boardId;
    private final Long id;
    private final String title;
    private final Integer order;

    private ListsResponseDto(Long boardId, Long id, String title, Integer order) {
        this.boardId = boardId;
        this.id = id;
        this.title = title;
        this.order = order;
    }

    public static ListsResponseDto from(Lists lists) {
        return new ListsResponseDto(
                lists.getBoard().getId(),
                lists.getId(),
                lists.getTitle(),
                lists.getOrder());
    }

}
