package com.sparta.spring_trello.domain.card.entity;

import com.sparta.spring_trello.domain.comment.entity.Comment;
import com.sparta.spring_trello.domain.common.entity.Timestamped;
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
public class Card extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String contents;
    private LocalDate deadline;

//    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
//    private List<Activity> activities;  // 활동 내역
//
//    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
//    private List<Comment> comments;     // 댓글

    public void updateCard(String title, String contents, LocalDate deadline) {
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
    }
}
