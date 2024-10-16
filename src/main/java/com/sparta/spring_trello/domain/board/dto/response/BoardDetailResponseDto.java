package com.sparta.spring_trello.domain.board.dto.response;

import com.sparta.spring_trello.domain.board.entity.Board;
import com.sparta.spring_trello.domain.card.dto.response.CardResponseDto;
import com.sparta.spring_trello.domain.card.entity.Card;
import com.sparta.spring_trello.domain.list.dto.response.ListsResponseDto;
import com.sparta.spring_trello.domain.list.entity.Lists;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardDetailResponseDto {

    private final Long boardId;
    private final String title;
    private final List<Lists> lists;
    private final List<Card> card;

    public BoardDetailResponseDto(Long boardId, String title, List<ListsResponseDto> listsResponse, List<CardResponseDto> cardResponse) {
        this.boardId = boardId;
        this.title = title;
        this.lists = new ArrayList<>();
        this.card = new ArrayList<>();
    }

    public static BoardDetailResponseDto of(Board board, List<Lists> lists, List<Card> card) {
        List<ListsResponseDto> listsResponse = lists.stream().map(ListsResponseDto::from).toList();
        List<CardResponseDto> cardResponse = card.stream().map(CardResponseDto::new).toList();
        return new BoardDetailResponseDto(
                board.getId(),
                board.getTitle(),
                listsResponse,
                cardResponse
        );
    }
}
