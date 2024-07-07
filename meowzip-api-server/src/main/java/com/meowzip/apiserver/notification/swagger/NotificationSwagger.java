package com.meowzip.apiserver.notification.swagger;

import com.meowzip.apiserver.global.response.CommonListResponse;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.notification.dto.response.NotificationResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

@Tag(name = "알림")
public interface NotificationSwagger {

    @Operation(summary = "알림 리스트 조회")
    CommonListResponse<NotificationResponseDTO> showNotifications(Principal principal);

    @Operation(summary = "알림 읽음 처리")
    CommonResponse<Void> readNotification(Principal principal, Long notificationId);
}
