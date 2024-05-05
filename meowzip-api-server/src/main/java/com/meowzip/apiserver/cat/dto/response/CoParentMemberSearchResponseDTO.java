package com.meowzip.apiserver.cat.dto.response;

import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record CoParentMemberSearchResponseDTO(

        @Schema(description = "사용자 고유키")
        Long memberId,

        @Schema(description = "사용자 프로필 사진")
        String imageUrl,

        @Schema(description = "사용자 닉네임")
        String nickname,

        @Schema(description = "요청 여부")
        boolean isRequested
) {
    public CoParentMemberSearchResponseDTO(Member member, boolean isRequested) {
        this(member.getId(), member.getProfileImage(), member.getNickname(), isRequested);
    }
}
