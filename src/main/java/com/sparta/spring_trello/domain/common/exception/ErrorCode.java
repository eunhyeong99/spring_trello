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
    READ_ONLY_MEMBER(1002, "READ_ONLY_MEMBER"),
    NOT_CARD_AUTHOR(1003, "NOT_CARD_AUTHOR"),
    NOT_COMMENT_AUTHOR(1004, "NOT_COMMENT_AUTHOR");

    private final Integer code;
    private final String message;
    }
