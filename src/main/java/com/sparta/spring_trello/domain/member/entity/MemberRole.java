package com.sparta.spring_trello.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    WORKSPACE(Authority.WORKSPACE), // 워크스페이스 멤버
    BOARD(Authority.BOARD), // 보드 멤버
    READONLY(Authority.READONLY); // 읽기 전용 멤버

    private final String memberRole;

    public static class Authority {
        public static final String WORKSPACE = "WORKSPACE";
        public static final String BOARD = "BOARD";
        public static final String READONLY = "READONLY";
    }
}