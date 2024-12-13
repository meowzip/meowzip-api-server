package com.meowzip.apiserver.profile.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema
@Builder
public record MyProfileInfoResponseDTO(

        String profileImageUrl,
        String nickname,
        boolean existsNewNotification,
        int catCount,
        int postCount,
        int bookmarkCount
) {
}
