package com.meowzip.apiserver.notification.swagger;

import com.meowzip.apiserver.global.request.PageRequest;
import com.meowzip.apiserver.global.response.CommonListResponseV2;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.notification.dto.response.CoParentNotificationResponseDTO;
import com.meowzip.apiserver.notification.dto.response.NotificationResponseDTO;
import com.meowzip.apiserver.notification.dto.response.NotificationValidationResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

@Tag(name = "알림")
public interface NotificationSwagger {

    @Operation(summary = "알림 리스트 조회")
    CommonListResponseV2<NotificationResponseDTO> showNotifications(Principal principal,
                                                                    PageRequest pageRequest);

    @Operation(summary = "공동냥육 알림 리스트 조회")
    CommonListResponseV2<CoParentNotificationResponseDTO> showCoParentNotifications(Principal principal,
                                                                                    PageRequest pageRequest);

    @Operation(summary = "알림 읽음 처리")
    CommonResponse<Void> readNotification(Principal principal, Long notificationId);

    @Operation(summary = "알림 모두 읽음 처리")
    CommonResponse<Void> readAllNotifications(Principal principal);

    @Operation(summary = "알림 유효성 검사")
    CommonResponse<NotificationValidationResDTO> validateNotification(Principal principal, Long notificationId);
}
