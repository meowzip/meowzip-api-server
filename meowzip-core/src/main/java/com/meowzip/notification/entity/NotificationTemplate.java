package com.meowzip.notification.entity;

import com.meowzip.entity.BaseTimeEntity;
import com.meowzip.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplate extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationCategory category;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationCode code;

    private String title;

    private String content;

    private String baseUrl;

    public NotificationHistory toNotification(Member receiver, String detail, String content, String... replacers) {
        return NotificationHistory.builder()
                .template(this)
                .receiver(receiver)
                .link(generateLink(detail))
                .title(generateTitle(replacers))
                .content(content)
                .build();
    }

    public String generateLink(String detailLink) {
        return baseUrl.replace("{{link}}", detailLink);
    }

    private String generateTitle(String... replacers) {
        String result = title;
        for (int i = 0; i < replacers.length; i++) {
            result = result.replace("{{" + i + "}}", replacers[i]);
        }
        return result;
    }
}
