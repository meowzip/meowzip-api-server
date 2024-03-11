package com.meowzip.apiserver.notification.service;

import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.member.entity.Member;
import com.meowzip.notification.entity.NotificationCode;
import com.meowzip.notification.entity.NotificationTemplate;
import com.meowzip.notification.repository.NotificationHistoryRepository;
import com.meowzip.notification.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationSendService {

    private final NotificationTemplateRepository notificationTemplateRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;

    @Transactional
    public void send(Member receiver, NotificationCode code, String link, String replacer) {
        NotificationTemplate template = notificationTemplateRepository.findByCode(code)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.NOTIFICATION_TEMPLATE_NOT_FOUND));

        notificationHistoryRepository.save(template.toNotification(receiver, link, replacer));
    }
}
