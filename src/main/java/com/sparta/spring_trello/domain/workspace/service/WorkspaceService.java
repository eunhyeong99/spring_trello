package com.sparta.spring_trello.domain.workspace.service;


import com.sparta.spring_trello.domain.workspace.entity.Workspace;
import com.sparta.spring_trello.domain.workspace.repository.WorkspaceRepository;
import com.sparta.spring_trello.entity.User;
import com.sparta.spring_trello.exception.CustomException;
import com.sparta.spring_trello.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sparta.spring_trello.entity.UserRole.ROLE_ADMIN;


@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    //워크 스페이스 생성
    public Workspace createWorkspace(String title, String description, User admin) {
        // 워크스페이스 생성
        Workspace workspace = new Workspace();
        workspace.setTitle(title);
        workspace.setDescription(description);

        return workspaceRepository.save(workspace);  // Workspace 객체를 반환해야 함
    }

    //워크스페이스 조회
    public Workspace getWorkspaceById(Long workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));
    }

    //유저조회
    public User getUserByEmail(Long workspaceId, String email) {
        List<User> users = workspaceRepository.findMembersByWorkspaceId(workspaceId);

        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }

        throw new IllegalArgumentException("해당하는 멤버가 없습니다");
    }

    //모든멤버 조회
    public List<User> getMembersByWorkspaceId(Long workspaceId) {
        return workspaceRepository.findMembersByWorkspaceId(workspaceId);
    }

    //워크 스페이스 수정
    public Workspace updateWorkspace(Long workspaceId, String title, String newDescription, User userRole) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                                                 .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));

        if (!userRole.getUserRole().equals(ROLE_ADMIN)) {
            throw new CustomException(ErrorCode.ACCESS_FAIL_BY_USER_ROLE);
        }

        workspace.setTitle(title);
        workspace.setDescription(newDescription);

        return workspace;
    }

    //워크 스페이스 삭제
    public void deleteWorkspace(Long workspaceId, User user) {
        workspaceRepository.deleteById(workspaceId);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                                                 .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));


        if (!user.getUserRole().equals(ROLE_ADMIN) && workspace.getMemberRoles().containsKey(user)) {
            workspaceRepository.deleteById(workspaceId);
        } else {
            throw new CustomException(ErrorCode.ACCESS_FAIL_BY_USER_ROLE);
        }
    }
}
