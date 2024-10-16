package com.sparta.spring_trello.domain.member.repository;

import com.sparta.spring_trello.domain.member.entity.Member;
import com.sparta.spring_trello.domain.user.entity.User;
import com.sparta.spring_trello.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserAndWorkspace(User user, Workspace workspace);

}
