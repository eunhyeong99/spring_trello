package com.sparta.spring_trello.domain.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements CodeInterface {

    OK(200, "OK"),
    CREATED(201, "CREATED"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    PAYLOAD_TOO_LARGE(400, "PAYLOAD_TOO_LARGE"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    READ_ONLY_USER_ERROR(401, "READ_ONLY_USER_ERROR"),
    NOT_FOUND(404, "NOT_FOUND"),
    NOT_FOUND_FILE(404, "NOT_FOUND_FILE"),
    UNSUPPORTED_MEDIA_TYPE(415, "UNSUPPORTED_MEDIA_TYPE"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),
    NOT_IMPLEMENTED(501, "NOT_IMPLEMENTED"),
    INTERNAL_SERVER_ERROR_UPLOAD(500, "INTERNAL_SERVER_ERROR_UPLOAD"),
    INTERNAL_SERVER_ERROR_DELETE(500, "INTERNAL_SERVER_ERROR_DELETE"),

    // 보드 응답 코드
    BOARD_NOT_FOUND(404,"보드를 찾을 수 없습니다."),
    LISTS_NOT_FOUND(404,"리스트를 찾을 수 없습니다."),
    SUCCESS(200,"정상 처리되었습니다."),
    USER_NOT_FOUND(404,"유저를 찾을 수 없습니다."),
    MEMBER_READ_ONLY(403,"권한이 없습니다."),
  
    READ_ONLY_MEMBER(1002, "READ_ONLY_MEMBER"),
    NOT_CARD_AUTHOR(1003, "NOT_CARD_AUTHOR"),
    NOT_COMMENT_AUTHOR(1004, "NOT_COMMENT_AUTHOR");

    private final Integer code;
    private final String message;
}
