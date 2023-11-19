package com.meowzip.apiserver.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDTO(

        @NotBlank(message = "패스워드는 필수 항목입니다.")
        String password,

        @NotBlank(message = "토큰은 필수 항목입니다.")
        String token
) {}
