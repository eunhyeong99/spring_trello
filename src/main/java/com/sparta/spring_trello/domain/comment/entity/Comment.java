package com.sparta.spring_trello.domain.comment.entity;

import com.sparta.spring_trello.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private String emoji;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    private Long userId;  // 작성자 ID
}
