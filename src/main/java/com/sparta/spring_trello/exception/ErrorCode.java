package com.sparta.spring_trello.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements CodeInterface {


    SUCCESS(0,"Success"),
    USER_ALREADY_EXIST(-1,"Already exist"),
    USER_SAVED_FAILED(-2,"USER_SAVED_FAILED"),
    USER_NOT_FOUND(-3,"USER_NOT_FOUND"),
    MIS_MATCH_PASSWORD(-4,"MIS_MATCH_PASSWORD"),
    ACCESS_FAIL_BY_USER_ROLE(-5, "ACCESS_FAIL_BY_USER_ROLE");

    private final Integer code;
    private final String message;

}
