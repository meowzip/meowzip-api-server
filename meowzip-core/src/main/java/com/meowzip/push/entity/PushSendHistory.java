package com.meowzip.push.entity;

import com.meowzip.entity.BaseTimeEntity;
import com.meowzip.fcm.entity.FcmToken;
import com.meowzip.member.entity.Member;
import com.meowzip.notification.entity.NotificationHistory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PushSendHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_history_id")
    private NotificationHistory notificationHistory;

    private String request;
    private String response;
    private String errorMessage;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    public static PushSendHistory create(Member receiver, FcmToken token, NotificationHistory notificationHistory, String request) {
        return PushSendHistory.builder()
                .receiver(receiver)
                .token(token.getToken())
                .notificationHistory(notificationHistory)
                .request(request)
                .status(Status.PENDING)
                .build();
    }

    public void updateResponse(String response) {
        this.response = response;
        this.status = Status.SUCCESS;
    }

    public void updateErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.status = Status.FAILURE;
    }
}
