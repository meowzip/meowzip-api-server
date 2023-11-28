package com.meowzip.apiserver.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema
public record SendPasswordResetEmailRequestDTO(

        @Schema(description = "비밀번호 재설정할 이메일 주소")
        @Email(message = "이메일 양식에 맞지 않습니다.")
        @NotBlank(message = "이메일은 필수 항목입니다.")
        String email
) {}
