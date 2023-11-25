package com.meowzip.apiserver.member.dto.response;

import com.meowzip.member.entity.Member;

public record MemberResponseDTO(
        String nickname,
        String profileImage
) {

    public MemberResponseDTO(Member member) {
        this(member.getNickname(), member.getProfileImage());
    }
}
