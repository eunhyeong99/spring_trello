package com.sparta.spring_trello.domain.workspace.repository;

import com.sparta.spring_trello.domain.user.entity.User;
import com.sparta.spring_trello.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    @Query("SELECT m FROM Workspace w JOIN w.members m WHERE w.id = :workspaceId")
    List<User> findMembersByWorkspaceId(@Param("workspaceId") Long workspaceId);

}
