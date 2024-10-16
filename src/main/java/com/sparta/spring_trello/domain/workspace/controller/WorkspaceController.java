package com.sparta.spring_trello.domain.workspace.controller;

import com.sparta.spring_trello.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.spring_trello.domain.workspace.entity.Workspace;
import com.sparta.spring_trello.domain.workspace.service.WorkspaceService;
import com.sparta.spring_trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    //워크스페이스 생성
    @PostMapping("/workspaces")
    public ResponseEntity<Workspace> createWorkspace(@RequestBody WorkspaceRequestDto workspaceDto, @AuthenticationPrincipal User user) {
        // createWorkspace 메서드가 Workspace 객체를 반환하도록 해야 함
        Workspace workspace = workspaceService.createWorkspace(workspaceDto.getTitle(), workspaceDto.getDescription(), user);
        return new ResponseEntity<>(workspace, HttpStatus.CREATED);
    }


    //워크스페이스 조회
    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable Long workspaceId) {
        Workspace workspace = workspaceService.getWorkspaceById(workspaceId);
        return new ResponseEntity<>(workspace, HttpStatus.OK);
    }


    //워크스페이스 수정
    @PutMapping("/workspaces/{workspaceId}")
    public ResponseEntity<Workspace> updateWorkspace(@PathVariable Long workspaceId, @RequestBody WorkspaceRequestDto workspaceRequestDto, @PathVariable User user) {
        Workspace updatedWorkspace = workspaceService.updateWorkspace(workspaceId, workspaceRequestDto.getTitle(), workspaceRequestDto.getDescription(), user);
        return new ResponseEntity<>(updatedWorkspace, HttpStatus.OK);
    }


    //워크스페이스 삭제
    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user) {
        workspaceService.deleteWorkspace(workspaceId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 워크스페이스 ID로 특정 유저 이메일로 유저 조회
    @GetMapping("/{workspaceid}/members/email")
    public ResponseEntity<User> getUserByEmail(@PathVariable Long id, @RequestParam String email) {
        User user = workspaceService.getUserByEmail(id, email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // 워크스페이스 ID로 모든 멤버 조회
    @GetMapping("/{workspaceid}/members")
    public ResponseEntity<List<User>> getMembersByWorkspaceId(@PathVariable Long id) {
        List<User> members = workspaceService.getMembersByWorkspaceId(id);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }
}
