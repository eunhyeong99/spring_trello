package com.sparta.spring_trello.domain.member.service;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.common.exception.CustomException;
import com.sparta.spring_trello.domain.common.exception.ErrorCode;
import com.sparta.spring_trello.domain.member.dto.request.MemberRequest;
import com.sparta.spring_trello.domain.member.dto.response.MemberResponse;
import com.sparta.spring_trello.domain.member.entity.Member;
import com.sparta.spring_trello.domain.member.entity.MemberRole;
import com.sparta.spring_trello.domain.member.repository.MemberRepository;
import com.sparta.spring_trello.domain.user.entity.User;
import com.sparta.spring_trello.domain.user.entity.UserRole;
import com.sparta.spring_trello.domain.user.repository.UserRepository;
import com.sparta.spring_trello.domain.workspace.entity.Workspace;
import com.sparta.spring_trello.domain.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private MemberService memberService;

    private AuthUser adminAuthUser;
    private User user;
    private Workspace workspace;
    private Member member;

    @BeforeEach
    void setUp() {
        // 관리자 AuthUser 설정
        adminAuthUser = new AuthUser(1L, "admin@example.com", UserRole.ROLE_ADMIN);

        // 테스트용 User 및 Workspace 설정
        user = new User("user@example.com", UserRole.ROLE_USER);
        workspace = new Workspace(1L, "Test Workspace");
        member = new Member(user, workspace, MemberRole.WORKSPACE);
    }

    @Test
    void addMember_Success() {
        MemberRequest memberRequest = new MemberRequest(user.getId(), MemberRole.BOARD);

        // User와 Workspace를 모킹
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(workspaceRepository.findById(workspace.getId())).thenReturn(Optional.of(workspace));
        when(memberRepository.findByUserAndWorkspace(user, workspace)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        // 멤버 추가 테스트
        MemberResponse response = memberService.addMember(adminAuthUser, workspace.getId(), memberRequest);

        // 결과 검증
        assertNotNull(response);
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(MemberRole.WORKSPACE, response.getMemberRole()); // 임의로 설정된 역할에 맞게 검증
    }

    @Test
    void addMember_UserNotFound() {
        MemberRequest memberRequest = new MemberRequest(999L, MemberRole.BOARD);

        // 존재하지 않는 유저 모킹
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // 예외 발생 검증
        CustomException exception = assertThrows(CustomException.class, () ->
                memberService.addMember(adminAuthUser, workspace.getId(), memberRequest));

        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateMemberRole_Success() {
        Long memberId = member.getId();
        MemberRole newRole = MemberRole.READONLY;

        // 역할 수정할 멤버 모킹
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        // 역할 수정 테스트
        MemberResponse response = memberService.updateMemberRole(adminAuthUser, memberId, newRole);

        // 결과 검증
        assertNotNull(response);
        assertEquals(newRole, response.getMemberRole());
    }

    @Test
    void removeMember_Success() {
        Long memberId = member.getId();

        // 삭제할 멤버 모킹
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // 멤버 삭제 테스트
        assertDoesNotThrow(() -> memberService.removeMember(adminAuthUser, memberId));
        verify(memberRepository, times(1)).delete(member);
    }

    @Test
    void removeMember_NotFound() {
        Long memberId = 999L;

        // 존재하지 않는 멤버 모킹
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // 예외 발생 검증
        CustomException exception = assertThrows(CustomException.class, () ->
                memberService.removeMember(adminAuthUser, memberId));

        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void addMember_UserAlreadyExists() {
        MemberRequest memberRequest = new MemberRequest(user.getId(), MemberRole.BOARD);

        // 이미 존재하는 유저 모킹
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(workspaceRepository.findById(workspace.getId())).thenReturn(Optional.of(workspace));
        when(memberRepository.findByUserAndWorkspace(user, workspace)).thenReturn(Optional.of(member));

        // 예외 발생 검증
        CustomException exception = assertThrows(CustomException.class, () ->
                memberService.addMember(adminAuthUser, workspace.getId(), memberRequest));

        assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
    }

    @Test
    void validateAdminAuthority_Unauthorized() {
        AuthUser nonAdminAuthUser = new AuthUser(2L, "user@example.com", UserRole.ROLE_USER);

        // 관리자 권한 없는 사용자의 요청 테스트
        CustomException exception = assertThrows(CustomException.class, () ->
                memberService.addMember(nonAdminAuthUser, workspace.getId(), new MemberRequest(user.getId(), MemberRole.BOARD)));

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
    }
}
