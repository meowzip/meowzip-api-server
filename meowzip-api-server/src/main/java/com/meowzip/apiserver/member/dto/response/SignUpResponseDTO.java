package com.meowzip.apiserver.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record SignUpResponseDTO(

        @Schema(description = "자동 생성된 닉네임")
        String generatedNickname,

        @Schema(description = "프로필 이미지 URL")
        String profileImage
) {}
