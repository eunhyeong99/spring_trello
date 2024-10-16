package com.sparta.spring_trello.domain.member.dto.response;

import com.sparta.spring_trello.domain.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {
    private final Long memberId;
    private final String email;
    private final MemberRole memberRole;
}
