package com.sparta.spring_trello.domain.workspace.controller;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.spring_trello.domain.workspace.entity.Workspace;
import com.sparta.spring_trello.domain.workspace.service.WorkspaceService;
import com.sparta.spring_trello.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    //워크스페이스 생성
    @PostMapping("/workspaces")
    public ResponseEntity<ApiResponse<?>> createWorkspace(
            @RequestBody @Valid WorkspaceRequestDto workspaceDto,
            @AuthenticationPrincipal AuthUser authUser) {

        // createWorkspace 메서드가 Workspace 객체를 반환하도록 해야 함
        Workspace workspace = workspaceService.createWorkspace(workspaceDto, authUser);

        // ApiResponse 객체를 사용하여 응답을 생성합니다.
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(workspace));
    }


    //워크스페이스 조회
    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<ApiResponse<?>> getWorkspaceById(@PathVariable(name = "workspaceId") Long workspaceId) {
        Workspace workspace = workspaceService.getWorkspaceById(workspaceId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(workspace));
    }


    //워크스페이스 수정
    @PutMapping("/workspaces/{workspaceId}")
    public ResponseEntity<ApiResponse<?>> updateWorkspace(
            @PathVariable(name = "workspaceId") Long workspaceId,
            @RequestBody @Valid WorkspaceRequestDto workspaceRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {
        Workspace updatedWorkspace = workspaceService.updateWorkspace(workspaceId, workspaceRequestDto, authUser);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(updatedWorkspace));
    }


    //워크스페이스 삭제
    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<ApiResponse<?>> deleteWorkspace(
            @PathVariable(name = "workspaceId") Long workspaceId,
            @AuthenticationPrincipal AuthUser authUser) {

        workspaceService.deleteWorkspace(workspaceId,authUser);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }

}
