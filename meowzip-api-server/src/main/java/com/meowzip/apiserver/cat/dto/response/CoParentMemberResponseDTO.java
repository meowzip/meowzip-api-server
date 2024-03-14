package com.meowzip.apiserver.cat.dto.response;

import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record CoParentMemberResponseDTO(

        @Schema(description = "사용자 고유키")
        Long memberId,

        @Schema(description = "사용자 프로필 사진")
        String imageUrl,

        @Schema(description = "사용자 닉네임")
        String nickname
) {

    public CoParentMemberResponseDTO(Member member) {
        this(member.getId(), member.getProfileImage(), member.getNickname());
    }
}
