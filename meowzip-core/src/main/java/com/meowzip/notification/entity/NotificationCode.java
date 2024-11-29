package com.meowzip.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationCode {

    MN001("COMMENT", "댓글 알림"),
    MN002("LIKE", "좋아요 알림"),
    MN003("DIARY", "공동냥육 일지 작성 알림"),
    MN004("COPARENT_REQUEST", "공동냥육 신청 수신 알림"),
    MN005("COPARENT", "공동냥육 신청 수락 알림"),
    MN006("COPARENT", "공동냥육 신청 거절 알림"),
    ;

    private final String type;
    private final String description;
}
