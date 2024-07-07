package com.meowzip.apiserver.notification.dto.response;

import com.meowzip.apiserver.global.util.DateTimeUtil;
import com.meowzip.notification.entity.NotificationHistory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record NotificationResponseDTO(

        @Schema(description = "알림 ID")
        long id,

        @Schema(description = "제목")
        String title,

        @Schema(description = "내용")
        String content,

        @Schema(description = "이동 링크")
        String link,

        @Schema(description = "생성일")
        String createdAt
) {

    public NotificationResponseDTO(NotificationHistory notification) {
        this(notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getLink(),
                DateTimeUtil.toRelative(notification.getCreatedAt())
        );
    }
}
