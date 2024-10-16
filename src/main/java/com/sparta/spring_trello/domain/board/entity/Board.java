package com.sparta.spring_trello.domain.board.entity;

import com.sparta.spring_trello.domain.common.entity.Timestamped;
import com.sparta.spring_trello.domain.list.entity.Lists;
import com.sparta.spring_trello.domain.workspace.entity.Work;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String backgroundColor;
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Work workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("order ASC")
    private final List<Lists> lists = new ArrayList<>();

    public Board(String title, String backgroundColor, String image) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.image = image;
    }

    // 리스트 수정
    public void updateBoard(String title, String backgroundColor, String image) {
        new Board(title, backgroundColor, image);
    }

}
