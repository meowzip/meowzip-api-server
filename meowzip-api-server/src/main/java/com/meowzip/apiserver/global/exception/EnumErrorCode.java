package com.meowzip.apiserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumErrorCode {

    SUCCESS(1, "success"),
    MEMBER_ALREADY_EXISTS(100001, "이미 존재하는 회원입니다."),
    TOKEN_REQUIRED(100002, "토큰이 없습니다."),
    TOKEN_EXPIRED(100003, "토큰이 만료되었습니다."),
    TOKEN_INVALID(100004, "유효하지 않은 토큰입니다."),
    MEMBER_NOT_FOUND(100005, "회원을 찾을 수 없습니다."),
    LOGIN_FAILED(100006, "아이디 또는 비밀번호를 확인해주세요.");

    private final int result;
    private final String message;
}
