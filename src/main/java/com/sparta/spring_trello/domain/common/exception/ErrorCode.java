package com.sparta.spring_trello.domain.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements CodeInterface {

    OK(200, "OK"),
    CREATED(201, "CREATED"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),
    NOT_IMPLEMENTED(501, "NOT_IMPLEMENTED"),


    // 보드 응답 코드
    BOARD_NOT_FOUND(404,"보드를 찾을 수 없습니다."),
    LISTS_NOT_FOUND(404,"리스트를 찾을 수 없습니다."),
    SUCCESS(200,"정상 처리되었습니다."),
    USER_NOT_FOUND(404,"유저를 찾을 수 없습니다."),
    MEMBER_READ_ONLY(403,"사용 권한이 없습니다."),

    READ_ONLY_MEMBER(1002, "READ_ONLY_MEMBER"),
    NOT_CARD_AUTHOR(1003, "NOT_CARD_AUTHOR"),
    NOT_COMMENT_AUTHOR(1004, "NOT_COMMENT_AUTHOR");

    private final Integer code;
    private final String message;
    }
