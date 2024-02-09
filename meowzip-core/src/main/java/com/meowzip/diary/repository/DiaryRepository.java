package com.meowzip.diary.repository;

import com.meowzip.diary.entity.Diary;
import com.meowzip.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findAllByMemberAndCaredAtBetween(Member member, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
