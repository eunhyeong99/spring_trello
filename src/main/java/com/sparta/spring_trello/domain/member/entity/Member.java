package com.sparta.spring_trello.domain.member.entity;

import com.sparta.spring_trello.domain.common.entity.Timestamped;
import com.sparta.spring_trello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "workspace_id"})
        }
) // user와 workspace의 조합이 중복되지 않도록 보장하기위해 유니크 제약 조건을 추가하였음.
public class Member extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "workspace_id")
//    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole memberRole;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

//    public Member(User user, Workspace workspace, MemberRole memberRole) {
//        this.user = user;
//        this.workspace = workspace;
//        this.memberRole = memberRole;
//    }

    // 멤버 역할 수정
    public void update(MemberRole memberRole) {
        this.memberRole = memberRole;
    }
}
