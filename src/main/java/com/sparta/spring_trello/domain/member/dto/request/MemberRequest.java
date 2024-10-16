package com.sparta.spring_trello.domain.member.dto.request;

import com.sparta.spring_trello.domain.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    private Long userId;  // 추가될 사용자 ID
    private MemberRole memberRole;
}
