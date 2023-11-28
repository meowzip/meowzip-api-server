package com.meowzip.apiserver.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema
public record ResetPasswordRequestDTO(

        @Schema(description = "변경할 닉네임")
        @NotBlank(message = "패스워드는 필수 항목입니다.")
        String password,

        @Schema(description = "URL에 포함된 토큰")
        @NotBlank(message = "토큰은 필수 항목입니다.")
        String token
) {}
