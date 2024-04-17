package com.meowzip.apiserver.community.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ModifyPostRequestDTO(

        @NotBlank(message = "글 내용은 필수입니다.")
        String content
) {
}
