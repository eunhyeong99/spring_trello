package com.sparta.spring_trello.domain.list.entity;

import com.sparta.spring_trello.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Lists {

        @Id
        @GeneratedValue
        private Long id;

        private String title;
        private String sort;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;


    //    @OneToMany(mappedBy = "lists", cascade = CascadeType.REMOVE)
    //    private final List<Card> card = new ArrayList<>();

}
