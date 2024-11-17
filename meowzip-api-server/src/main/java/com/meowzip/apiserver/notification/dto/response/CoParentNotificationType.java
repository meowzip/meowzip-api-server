package com.meowzip.apiserver.notification.dto.response;

import com.meowzip.notification.entity.NotificationCode;

import java.util.List;

public enum CoParentNotificationType {

    DIARY,
    REQUEST,
    ;

    public static CoParentNotificationType from(NotificationCode code) {
        if (List.of(NotificationCode.MN001, NotificationCode.MN002).contains(code)) {
            return null;
        }

        return switch (code) {
            case MN003 -> DIARY;
            case MN004, MN005, MN006 -> REQUEST;
            default -> throw new IllegalArgumentException("Unknown notification code: " + code);
        };
    }
}
