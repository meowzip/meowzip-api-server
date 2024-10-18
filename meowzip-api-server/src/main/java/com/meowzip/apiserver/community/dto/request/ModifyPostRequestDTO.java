package com.meowzip.apiserver.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ModifyPostRequestDTO(

        @Schema(description = "글 내용")
        @NotBlank(message = "글 내용은 필수입니다.")
        String content,

        @Schema(description = "이미지 url 목록")
        List<String> imageUrls
) {
}
