package com.meowzip.apiserver.notification.service;

import com.meowzip.apiserver.cat.service.CoParentService;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.notification.dto.response.CoParentNotificationResponseDTO;
import com.meowzip.apiserver.notification.dto.response.NotificationResponseDTO;
import com.meowzip.member.entity.Member;
import com.meowzip.notification.entity.NotificationCategory;
import com.meowzip.notification.entity.NotificationHistory;
import com.meowzip.notification.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationHistoryRepository notificationHistoryRepository;
    private final CoParentService coParentService;

    public List<NotificationResponseDTO> showNotifications(Member member) {
        LocalDateTime criteria = LocalDateTime.now().minusWeeks(8);

        return notificationHistoryRepository.findByReceiverAndCreatedAtAfterOrderByCreatedAtDesc(member, criteria).stream()
                .filter(notification -> notification.getTemplate().getCategory() == NotificationCategory.COMMUNITY)
                .map(NotificationResponseDTO::new)
                .toList();
    }

    @Transactional
    public void read(Member member, Long notificationId) {
        var notification = notificationHistoryRepository.findByReceiverAndId(member, notificationId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.NOTIFICATION_HISTORY_NOT_FOUND));

        if (!isOwner(member, notification)) {
            throw new IllegalArgumentException("You are not the owner of this notification");
        }

        notification.read();
    }

    private boolean isOwner(Member member, NotificationHistory notification) {
        return notification.getReceiver().equals(member);
    }

    public boolean isExistsUnreadNotification(Member member) {
        return notificationHistoryRepository.existsByReceiverAndReadAtIsNull(member);
    }

    public List<CoParentNotificationResponseDTO> showCoParentNotifications(Member member) {
        LocalDateTime criteria = LocalDateTime.now().minusWeeks(8);

        return notificationHistoryRepository.findByReceiverAndCreatedAtAfterOrderByCreatedAtDesc(member, criteria).stream()
                .filter(notification -> notification.getTemplate().getCategory() == NotificationCategory.COPARENTING)
                .map(notification -> {
                    Long coParentId = notification.getDetailLink();
                    boolean isResponded = false;
                    boolean isExpired = true;
                    if (coParentId != null) {
                        isResponded = coParentService.isResponded(coParentId);
                        isExpired = coParentService.isExpired(coParentId);
                    }

                    return new CoParentNotificationResponseDTO(notification, isExpired, isResponded);
                })
                .toList();
    }
}
