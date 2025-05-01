package com.meowzip.push.repository;

import com.meowzip.push.entity.PushSendHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushSendHistoryRepository extends JpaRepository<PushSendHistory, Long> {
}
