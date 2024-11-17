package com.meowzip.apiserver.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meowzip.apiserver.global.util.DateTimeUtil;
import com.meowzip.notification.entity.NotificationHistory;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CoParentNotificationResponseDTO(

        @Schema(description = "알림 ID")
        long id,

        @Schema(description = "알림 타입")
        CoParentNotificationType type,

        @Schema(description = "제목")
        String title,

        @Schema(description = "알림 보낸 사람 닉네임")
        String senderNickname,

        @Schema(description = "이동 링크")
        String link,

        @Schema(description = "읽음 여부")
        boolean isRead,

        @Schema(description = "수락 가능 기간 지났는지 여부")
        boolean isExpired,

        @Schema(description = "응답한 메시지인지 여부")
        boolean isResponded,

        @Schema(description = "생성일")
        String createdAt
) {

    public CoParentNotificationResponseDTO(NotificationHistory notification, boolean isExpired, boolean isResponded) {
        this(notification.getId(),
                CoParentNotificationType.from(notification.getTemplate().getCode()),
                notification.getTitle(),
                notification.getSenderNickname(),
                notification.getLink(),
                notification.isRead(),
                isExpired,
                isResponded,
                DateTimeUtil.toRelative(notification.getCreatedAt())
        );
    }
}
