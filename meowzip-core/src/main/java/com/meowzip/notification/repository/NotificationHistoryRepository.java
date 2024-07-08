package com.meowzip.notification.repository;

import com.meowzip.member.entity.Member;
import com.meowzip.notification.entity.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {

    List<NotificationHistory> findByReceiverAndCreatedAtAfterOrderByCreatedAtDesc(Member receiver, LocalDateTime createdAt);

    Optional<NotificationHistory> findByReceiverAndId(Member receiver, Long id);

    boolean existsByReceiverAndReadAtIsNull(Member member);
}
