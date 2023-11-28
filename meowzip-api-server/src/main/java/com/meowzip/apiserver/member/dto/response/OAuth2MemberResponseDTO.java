package com.meowzip.apiserver.member.dto.response;

import com.meowzip.member.entity.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record OAuth2MemberResponseDTO(

        @Schema(description = "가입된 SNS 타입")
        LoginType loginType
) {
}
