package com.meowzip.apiserver.notification.dto.response;

import com.meowzip.apiserver.global.util.DateTimeUtil;
import com.meowzip.notification.entity.NotificationHistory;
import io.swagger.v3.oas.annotations.media.Schema;

public record CoParentNotificationResponseDTO(

        @Schema(description = "알림 ID")
        long id,

        @Schema(description = "알림 타입")
        CoParentNotificationType type,

        @Schema(description = "제목")
        String title,

        @Schema(description = "내용")
        String content,

        @Schema(description = "이동 링크")
        String link,

        @Schema(description = "읽음 여부")
        boolean isRead,

        @Schema(description = "생성일")
        String createdAt
) {

    public CoParentNotificationResponseDTO(NotificationHistory notification) {
        this(notification.getId(),
                CoParentNotificationType.from(notification.getTemplate().getCode()),
                notification.getTitle(),
                notification.getContent(),
                notification.getLink(),
                notification.isRead(),
                DateTimeUtil.toRelative(notification.getCreatedAt())
        );
    }
}
