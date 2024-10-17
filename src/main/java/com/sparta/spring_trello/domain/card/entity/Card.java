package com.sparta.spring_trello.domain.card.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.spring_trello.domain.board.entity.Board;
import com.sparta.spring_trello.domain.comment.entity.Comment;
import com.sparta.spring_trello.domain.common.entity.Timestamped;
import com.sparta.spring_trello.domain.list.entity.Lists;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "card", indexes = {
        @Index(name = "idx_card_title", columnList = "title"),
        @Index(name = "idx_card_board_id", columnList = "board_id"),
        @Index(name = "idx_card_user_id", columnList = "user_id")
})
public class Card extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String contents;
    private LocalDate deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities;  // 활동 내역

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;     // 댓글

    @Column(name = "user_id", nullable = false) // 작성자 ID 추가
    private Long userId; // 작성자 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lists_id", nullable = false)
    private Lists lists;

    public void updateCard(String title, String contents, LocalDate deadline) {
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
    }
}
