package com.meowzip.apiserver.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meowzip.member.entity.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record EmailExistsResponseDTO(

        @Schema(description = "이메일로 가입했는지 여부")
        boolean isEmailExists,

        @Schema(nullable = true,
                description = "가입된 SNS 타입, isEmailExists가 true일 경우에만 not null")
        LoginType loginType
) {}
