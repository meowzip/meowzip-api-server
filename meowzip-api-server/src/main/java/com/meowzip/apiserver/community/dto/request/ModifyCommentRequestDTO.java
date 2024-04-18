package com.meowzip.apiserver.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ModifyCommentRequestDTO(

        @Schema(description = "댓글 내용")
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content
) {
}
