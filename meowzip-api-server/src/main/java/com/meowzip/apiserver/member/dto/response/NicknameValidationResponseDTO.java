package com.meowzip.apiserver.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record NicknameValidationResponseDTO(

        @Schema(description = "닉네임 사용 가능 여부")
        boolean isNicknameAvailable
) {
}
