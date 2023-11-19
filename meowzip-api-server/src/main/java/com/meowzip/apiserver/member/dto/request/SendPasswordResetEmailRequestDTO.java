package com.meowzip.apiserver.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendPasswordResetEmailRequestDTO(
        @Email(message = "이메일 양식에 맞지 않습니다.")
        @NotBlank(message = "이메일은 필수 항목입니다.")
        String email
) {}
