package com.meowzip.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginType {

    EMAIL,
    GOOGLE,
    APPLE,
    KAKAO
}
