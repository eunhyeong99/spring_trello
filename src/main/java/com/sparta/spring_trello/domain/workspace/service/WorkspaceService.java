package com.sparta.spring_trello.domain.workspace.service;


import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.common.exception.CustomException;
import com.sparta.spring_trello.domain.common.exception.ErrorCode;
import com.sparta.spring_trello.domain.user.entity.UserRole;
import com.sparta.spring_trello.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.spring_trello.domain.workspace.entity.Workspace;
import com.sparta.spring_trello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;


    //워크 스페이스 생성
    public Workspace createWorkspace(WorkspaceRequestDto workspace, AuthUser authUser) {

        // ADMIN권한을 가진 유저만 생성 할 수 있다.
        if(!authUser.getUserRole().equals(UserRole.ROLE_ADMIN)){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 워크스페이스 생성
        Workspace newWorkspace = new Workspace(workspace.getTitle(),workspace.getDescription());

        return workspaceRepository.save(newWorkspace);  // Workspace 객체를 반환해야 함
    }

    //워크스페이스 조회
    public Workspace getWorkspaceById(Long workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORKSPACE_NOT_FOUND));
    }

    //워크 스페이스 수정
    public Workspace updateWorkspace(Long workspaceId, WorkspaceRequestDto workspaceRequestDto, AuthUser authUser) {

        Workspace findWorkspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORKSPACE_NOT_FOUND));

        // ADMIN권한을 가진 유저만 수정 할 수 있다.
        if(!authUser.getUserRole().equals(UserRole.ROLE_ADMIN)){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        findWorkspace.setTitle(workspaceRequestDto.getTitle());
        findWorkspace.setDescription(workspaceRequestDto.getDescription());

        workspaceRepository.save(findWorkspace);

        return findWorkspace;
    }

    //워크 스페이스 삭제
    public void deleteWorkspace(Long workspaceId, AuthUser authUser) {
        // ADMIN권한을 가진 유저만 삭제 할 수 있다.
        if(!authUser.getUserRole().equals(UserRole.ROLE_ADMIN)){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORKSPACE_NOT_FOUND));

        workspaceRepository.deleteById(workspaceId);
    }
}