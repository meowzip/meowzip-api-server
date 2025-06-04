package com.meowzip.apiserver.notification.dto.response;

import com.meowzip.apiserver.global.exception.EnumErrorCode;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record NotificationValidationResDTO(

        boolean isValid,
        String message
) {

    public static NotificationValidationResDTO valid() {
        return new NotificationValidationResDTO(true, EnumErrorCode.SUCCESS.getMessage());
    }

    public static NotificationValidationResDTO invalid(String message) {
        return new NotificationValidationResDTO(false, message);
    }
}
