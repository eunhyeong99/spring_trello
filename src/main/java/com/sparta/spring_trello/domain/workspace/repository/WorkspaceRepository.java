package com.sparta.spring_trello.domain.workspace.repository;

import com.sparta.spring_trello.domain.workspace.entity.Workspace;
import com.sparta.spring_trello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    List<Workspace> findMembersByEmail(String email);

    List<Workspace> findById(String title);

    @Query("SELECT w FROM Workspace w JOIN FETCH w.members WHERE w.workspaceId = :workspaceId")
    List<User> findMembersByWorkspaceId(@Param("workspaceId") Long workspaceId);
}
