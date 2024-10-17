package com.sparta.spring_trello.domain.workspace.entity;

import com.sparta.spring_trello.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함한 생성자 자동 생성
@Table(name = "workspaces")
public class Workspace {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45)
    private String title;

    @Column
    private String description;

    @OneToMany(mappedBy = "workspace",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Member> members = new ArrayList<>();


    public Workspace(String title,String description) {
        this.title = title;
        this.description = description;
    }
}
