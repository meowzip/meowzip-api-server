package com.meowzip.notification.repository;

import com.meowzip.notification.entity.NotificationCode;
import com.meowzip.notification.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    Optional<NotificationTemplate> findByCode(NotificationCode code);
}
