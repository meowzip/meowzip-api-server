package com.meowzip.apiserver.notification.service;

import com.meowzip.apiserver.cat.service.CoParentService;
import com.meowzip.apiserver.community.service.CommunityPostService;
import com.meowzip.apiserver.diary.service.DiaryService;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.global.response.CommonListResponseV2;
import com.meowzip.apiserver.notification.dto.response.CoParentNotificationResponseDTO;
import com.meowzip.apiserver.notification.dto.response.NotificationResponseDTO;
import com.meowzip.apiserver.notification.dto.response.NotificationValidationResDTO;
import com.meowzip.coparent.entity.CoParent;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationHistoryRepository notificationHistoryRepository;
    private final CoParentService coParentService;
    private final CommunityPostService communityPostService;
    private final DiaryService diaryService;

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
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        notification.read();
    }

    public NotificationValidationResDTO validateNotification(Member receiver, Long notificationId) {
        var notification = notificationHistoryRepository.findByReceiverAndId(receiver, notificationId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.NOTIFICATION_HISTORY_NOT_FOUND));

        Long contentId = notification.getDetailLink();
        if (!notification.isCoParentResponseNotification() && contentId == null) {
            return NotificationValidationResDTO.invalid(EnumErrorCode.BAD_REQUEST.getMessage());
        }

        return switch (notification.getTemplate().getCode()) {
            case MN001, MN002 -> validateCommunityPost(contentId);
            case MN003 -> validateDiary(contentId);
            case MN004 -> validateCoParentByParticipant(notification.getReceiver(), contentId);
            case MN005, MN006 -> NotificationValidationResDTO.valid();
        };
    }

    private NotificationValidationResDTO validateCommunityPost(Long postId) {
        return communityPostService.getPostIfExists(postId).isPresent()
                ? NotificationValidationResDTO.valid()
                : NotificationValidationResDTO.invalid(EnumErrorCode.POST_NOT_FOUND.getMessage());
    }

    private NotificationValidationResDTO validateDiary(Long diaryId) {
        return diaryService.getDiaryById(diaryId).isPresent()
                ? NotificationValidationResDTO.valid()
                : NotificationValidationResDTO.invalid(EnumErrorCode.DIARY_NOT_FOUND.getMessage());
    }

    private NotificationValidationResDTO validateCoParentByParticipant(Member receiver, Long coParentId) {
        Optional<CoParent> coParent = coParentService.getByParticipantAndCoParentId(receiver, coParentId);

        if (coParent.isEmpty()) {
            return NotificationValidationResDTO.invalid(EnumErrorCode.CO_PARENT_NOT_FOUND.getMessage());
        }

        if (coParent.get().isCanceled()) {
            return NotificationValidationResDTO.invalid(EnumErrorCode.CO_PARENT_NOT_FOUND.getMessage());
        }

        return NotificationValidationResDTO.valid();
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

    @Transactional
    public void readAll(Member member) {
        var notificationHistories = notificationHistoryRepository.findByReceiverAndReadAtIsNull(member);
        notificationHistories.forEach(NotificationHistory::read);
    }
}
