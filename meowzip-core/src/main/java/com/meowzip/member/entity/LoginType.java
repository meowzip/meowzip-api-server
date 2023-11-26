package com.meowzip.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginType {

    EMAIL(false),
    GOOGLE(true),
    APPLE(true),
    KAKAO(true)
    ;

    private final boolean requiresRemoteAuthorization;

    public static LoginType getLoginType(String registrationId) {
        return switch (registrationId) {
            case "google" -> GOOGLE;
            case "kakao" -> KAKAO;
            // TODO: APPLE 추가
            default -> EMAIL;
        };
    }
}
