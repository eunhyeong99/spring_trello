package com.sparta.spring_trello.domain.member.controller;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.member.dto.request.MemberRequest;
import com.sparta.spring_trello.domain.member.dto.response.MemberResponse;
import com.sparta.spring_trello.domain.member.entity.MemberRole;
import com.sparta.spring_trello.domain.member.service.MemberService;
import com.sparta.spring_trello.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/members")
public class MemberController {
    private final MemberService memberService;

    // 멤버 추가
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> addMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("workspaceId") Long workspaceId,
            @RequestBody MemberRequest memberRequest
    ) {
        return ResponseEntity.ok(ApiResponse.success(memberService.addMember(authUser, workspaceId, memberRequest)));
    }

    // 역할 수정
    @PutMapping("/{memberId}/role")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMemberRole(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("memberId") Long memberId,
            @RequestParam("role") MemberRole role
    ) {
        return ResponseEntity.ok(ApiResponse.success(memberService.updateMemberRole(authUser, memberId, role));
    }

    // 멤버 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<?>> removeMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("memberId") Long memberId
    ) {
        memberService.removeMember(authUser, memberId);
        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
