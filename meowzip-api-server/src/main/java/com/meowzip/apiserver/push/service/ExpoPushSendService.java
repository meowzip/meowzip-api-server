package com.meowzip.apiserver.push.service;

import com.meowzip.apiserver.push.dto.request.ExpoPushMessageRQ;
import com.meowzip.fcm.entity.FcmToken;
import com.meowzip.fcm.repository.FcmTokenRepository;
import com.meowzip.notification.entity.NotificationHistory;
import com.meowzip.push.entity.PushSendHistory;
import com.meowzip.push.repository.PushSendHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
public class ExpoPushSendService {

    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";
    private final WebClient webClient;
    private final FcmTokenRepository fcmTokenRepository;
    private final PushSendHistoryRepository pushSendHistoryRepository;

    public ExpoPushSendService(WebClient.Builder webClientBuilder, FcmTokenRepository fcmTokenRepository,
                               PushSendHistoryRepository pushSendHistoryRepository) {

        this.webClient = webClientBuilder.baseUrl(EXPO_PUSH_URL).build();
        this.fcmTokenRepository = fcmTokenRepository;
        this.pushSendHistoryRepository = pushSendHistoryRepository;
    }

    @Transactional
    public void sendNotification(NotificationHistory notification) {
        List<FcmToken> expoPushTokens = fcmTokenRepository.findByMemberId(notification.getReceiver().getId());
        if (expoPushTokens.isEmpty()) {
            log.warn("No Expo push tokens found for member ID: {}", notification.getReceiver().getId());
            return;
        }

        for (FcmToken expoPushToken : expoPushTokens) {
            if (!expoPushToken.isValidExpoToken()) {
                log.warn("Invalid Expo Push Token");
                continue;
            }

            var message = ExpoPushMessageRQ.of(expoPushToken.getToken(), notification);
            var pushSendHistory = PushSendHistory.create(notification.getReceiver(), expoPushToken, notification, message.toString());
            pushSendHistoryRepository.save(pushSendHistory);

            webClient.post()
                    .uri(EXPO_PUSH_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(message)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnNext(response -> {
                        log.info("Expo push response: {}", response);
                        pushSendHistory.updateResponse(response);
                    })
                    .doOnError(error -> {
                        log.error("Push failed: {}", error.getMessage());
                        pushSendHistory.updateErrorMessage(error.getMessage());
                    })
                    .block();
        }
    }
}
