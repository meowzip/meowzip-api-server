package com.meowzip.apiserver.push.dto.request;

import com.meowzip.notification.entity.NotificationHistory;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.Map;

@Builder(access = AccessLevel.PRIVATE)
public record ExpoPushMessageRQ(

        String to,
        String sound,
        String title,
        String body,
        Map<String, String> data
) {

    public static ExpoPushMessageRQ of(String to, NotificationHistory notification) {
        return ExpoPushMessageRQ.builder()
                .to(to)
                .sound("default")
                .title("") // 수정필요
                .body(notification.getPushBody())
                .data(Map.of("url", notification.getLink(),
                        "notification-id", notification.getId().toString(),
                        "type", notification.getTemplate().getCategory().name()))
                .build();
    }
}
