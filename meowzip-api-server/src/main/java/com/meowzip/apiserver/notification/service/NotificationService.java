package com.meowzip.apiserver.notification.service;

import com.meowzip.apiserver.cat.service.CoParentService;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.global.response.CommonListResponseV2;
import com.meowzip.apiserver.notification.dto.response.CoParentNotificationResponseDTO;
import com.meowzip.apiserver.notification.dto.response.NotificationResponseDTO;
import com.meowzip.member.entity.Member;
import com.meowzip.notification.entity.NotificationCategory;
import com.meowzip.notification.entity.NotificationHistory;
import com.meowzip.notification.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationHistoryRepository notificationHistoryRepository;
    private final CoParentService coParentService;

    public CommonListResponseV2<NotificationResponseDTO> showNotifications(Member member, Pageable pageable) {
        LocalDateTime criteria = LocalDateTime.now().minusWeeks(8);

        var notificationHistories = notificationHistoryRepository.findByReceiverAndCreatedAtAfterOrderByCreatedAtDesc(member, criteria, NotificationCategory.COMMUNITY, pageable);
        var responseDTOs = notificationHistories.getContent().stream()
                .map(NotificationResponseDTO::new)
                .toList();

        return new CommonListResponseV2<NotificationResponseDTO>(HttpStatus.OK).add(responseDTOs, notificationHistories.hasNext());
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
        LocalDateTime criteria = LocalDateTime.now().minusWeeks(8);

        return notificationHistoryRepository.existsByReceiverAndReadAtIsNullAndCreatedAtAfter(member, criteria);
    }

    public CommonListResponseV2<CoParentNotificationResponseDTO> showCoParentNotifications(Member member, Pageable pageable) {
        LocalDateTime criteria = LocalDateTime.now().minusWeeks(8);

        var notificationHistories = notificationHistoryRepository.findByReceiverAndCreatedAtAfterOrderByCreatedAtDesc(member, criteria, NotificationCategory.COPARENTING, pageable);
        var responseDTOs = notificationHistories.getContent().stream()
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

        return new CommonListResponseV2<CoParentNotificationResponseDTO>(HttpStatus.OK).add(responseDTOs, notificationHistories.hasNext());
    }
}
