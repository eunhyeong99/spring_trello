package com.sparta.spring_trello.domain.board.entity;

import com.sparta.spring_trello.domain.list.entity.Lists;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String backgroundColor;
    private String image;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "workspace_id", nullable = false)
//    private Workspace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private final List<Lists> lists = new ArrayList<>();


}
