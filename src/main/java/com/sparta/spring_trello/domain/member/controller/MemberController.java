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

    /**
     * 멤버 추가
     * @param authUser 로그인한 유저(=관리자)
     * @param workspaceId 워크스페이스 ID
     * @param memberRequest 멤버 생성에 필요한 정보(멤버가 될 유저의 ID, 멤버의 역할)
     * @return MemberResponse 생성된 멤버의 정보(멤버ID, 멤버가 될 유저의 email, 멤버의 역할)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> addMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("workspaceId") Long workspaceId,
            @RequestBody MemberRequest memberRequest
    ) {
        return ResponseEntity.ok(ApiResponse.success(memberService.addMember(authUser, workspaceId, memberRequest)));
    }

    /**
     * 멤버 역할 수정
     * @param authUser 로그인한 유저(=관리자)
     * @param memberId 수정할 멤버 ID
     * @param role 수정할 멤버의 역할
     * @return MemberResponse 수정된 멤버의 정보(멤버ID, 멤버가 될 유저의 email, 멤버의 역할)
     */
    @PutMapping("/{memberId}/role")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMemberRole(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("memberId") Long memberId,
            @RequestParam("role") MemberRole role
    ) {
        return ResponseEntity.ok(ApiResponse.success(memberService.updateMemberRole(authUser, memberId, role)));
    }

    /**
     * 멤버 삭제
     * @param authUser 로그인한 유저(=관리자)
     * @param memberId 삭제할 멤버 ID
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<?>> removeMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("memberId") Long memberId
    ) {
        memberService.removeMember(authUser, memberId);
        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
