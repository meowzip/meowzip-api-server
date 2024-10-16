package com.meowzip.apiserver.profile.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema
@Builder
public record ProfileInfoResponseDTO(
        String profileImageUrl,
        String nickname,
        int catCount,
        int postCount
) {
}
