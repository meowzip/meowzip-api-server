package com.meowzip.apiserver.member.dto;

import com.meowzip.member.entity.LoginType;
import com.meowzip.member.entity.Member;

public record UserProfile(
        String id,
        String email
) {

    // TODO: registrationId 추가 필요 여부 확인
    public Member toMember(LoginType loginType, String nickname) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .loginType(loginType)
                .status(Member.Status.ACTIVE)
                .build();
    }
}
