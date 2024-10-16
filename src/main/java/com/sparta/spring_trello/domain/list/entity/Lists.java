package com.sparta.spring_trello.domain.list.entity;

import com.sparta.spring_trello.domain.board.entity.Board;
import com.sparta.spring_trello.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Lists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lists_id")
    private Long id;

    private String title;
    @Column(name = "order_Number")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // 카드 연관 관계 - 리스트 삭제 시 관련 카드도 삭제됨
    @OneToMany(mappedBy = "lists", cascade = CascadeType.REMOVE,orphanRemoval = true)
    private final List<Card> card = new ArrayList<>();

    public Lists(String title, Integer order) {
        this.title = title;
        this.order = order;
    }

    // 리스트 수정
    public void updateLists(String title, Integer order) {
        new Lists(title, order);
    }

}
