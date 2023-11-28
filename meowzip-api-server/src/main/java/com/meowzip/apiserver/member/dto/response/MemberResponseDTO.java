package com.meowzip.apiserver.member.dto.response;

import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record MemberResponseDTO(

        @Schema(description = "닉네임")
        String nickname,

        @Schema(description = "프로필 이미지 URL")
        String profileImage
) {

    public MemberResponseDTO(Member member) {
        this(member.getNickname(), member.getProfileImage());
    }
}
