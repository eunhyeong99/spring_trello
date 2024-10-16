package com.sparta.spring_trello.domain.workspace.entity;

import com.sparta.spring_trello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import javax.management.relation.Role;
import java.util.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함한 생성자 자동 생성
@Table(name = "workspaces")
public class Workspace {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workspaceId;

    @Column
    private String email;

    @Column(length = 45)
    private String title;

    @Column
    private String description;

    @ManyToMany
    @JoinTable(
            name = "workspace_user",
            joinColumns = @JoinColumn(name = "workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members = new ArrayList<>();

    // 워크스페이스에서 멤버별 역할을 지정하는 필드 추가
    @ElementCollection
    @CollectionTable(name = "workspaces", joinColumns = @JoinColumn(name = "workspace_id"))
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "role")
    private Map<User, Role> memberRoles = new HashMap<>();


    public Workspace(Long workspaceId, String title, String description) {
        this.workspaceId = workspaceId;
        this.title = title;
        this.description = description;
    }
}
