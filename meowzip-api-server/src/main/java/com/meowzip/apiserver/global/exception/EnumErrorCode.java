package com.meowzip.apiserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumErrorCode {

    SUCCESS(1, "success"),
    MEMBER_ALREADY_EXISTS(100001, "이미 존재하는 회원입니다."),
    ;

    private final int result;
    private final String message;
}
