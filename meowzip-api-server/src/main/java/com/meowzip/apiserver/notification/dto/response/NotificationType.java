package com.meowzip.apiserver.notification.dto.response;

public enum NotificationType {

    COMMENT,
    LIKE,
    DIARY,
    COPARENT_REQUEST,
    COPARENT,

    ;

    public static NotificationType from(String type) {
        for (var v : values()) {
            if (v.name().equals(type)) {
                return v;
            }
        }

        throw new IllegalArgumentException(type + " is not a valid notification type");
    }
}
