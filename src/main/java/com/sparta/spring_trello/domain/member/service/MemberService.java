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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    // 멤버 추가
    @Transactional
    public MemberResponse addMember(AuthUser authUser, Long workspaceId, MemberRequest memberRequest) {
        // 로그인한 사용자가 관리자인지 확인
        validateAdminAuthority(authUser);

        // 추가할 사용자가 존재하는지 확인
        User user = userRepository.findById(memberRequest.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 추가할 워크스페이스가 존재하는지 확인
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORKSPACE_NOT_FOUND));

        // 이미 등록된 멤버인지 확인
        if (memberRepository.findByUserAndWorkspace(user, workspace).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_MEMBER);
        }

        // 멤버 생성 후 DB에 저장
        Member newMember = new Member(user, workspace, memberRequest.getMemberRole());
        Member savedMember = memberRepository.save(newMember);

        // MemberResponse 반환
        return new MemberResponse(savedMember.getId(), user.getEmail(), savedMember.getMemberRole());
    }

    // 역할 수정
    @Transactional
    public MemberResponse updateMemberRole(AuthUser authUser, Long memberId, MemberRole newRole) {
        // 로그인한 사용자가 관리자인지 확인
        validateAdminAuthority(authUser);

        // 역할 수정할 멤버가 존재하는지 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 멤버 역할 수정
        member.update(newRole);

        // DB에 수정된 멤버 저장
        Member updatedMember = memberRepository.save(member);

        // 수정된 멤버 정보를 Dto로 반환
        return new MemberResponse(updatedMember.getId(), updatedMember.getUser().getEmail(), updatedMember.getMemberRole());
    }

    // 멤버 삭제
    @Transactional
    public void removeMember(AuthUser authUser, Long memberId) {
        // 로그인한 사용자가 관리자인지 확인
        validateAdminAuthority(authUser);

        // 삭제할 멤버가 존재하는지 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 해당 멤버 삭제
        memberRepository.delete(member);
    }

    // 로그인한 사용자가 관리자 예외처리 메서드
    private void validateAdminAuthority(AuthUser authUser) {
        if (authUser.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRole.ROLE_ADMIN.name()))) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
}
