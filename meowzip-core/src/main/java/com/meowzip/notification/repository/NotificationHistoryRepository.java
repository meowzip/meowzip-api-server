package com.meowzip.notification.repository;

import com.meowzip.member.entity.Member;
import com.meowzip.notification.entity.NotificationCategory;
import com.meowzip.notification.entity.NotificationHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {

    @Query("SELECT n FROM NotificationHistory n " +
            "JOIN n.template nt " +
            "WHERE n.receiver = :receiver AND n.createdAt > :createdAt AND nt.category = :category " +
            "ORDER BY n.createdAt DESC")
    Page<NotificationHistory> findByReceiverAndCreatedAtAfterOrderByCreatedAtDesc(@Param(("receiver")) Member receiver,
                                                                                  @Param("createdAt") LocalDateTime createdAt,
                                                                                  @Param("category") NotificationCategory category,
                                                                                  Pageable pageable);

    Optional<NotificationHistory> findByReceiverAndId(Member receiver, Long id);

    boolean existsByReceiverAndReadAtIsNull(Member member);
}
