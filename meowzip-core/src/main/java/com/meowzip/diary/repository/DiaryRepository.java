package com.meowzip.diary.repository;

import com.meowzip.diary.entity.Diary;
import com.meowzip.diary.entity.MonthlyDiaryInterface;
import com.meowzip.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findAllByMemberAndCaredDate(Member member, LocalDate caredDate, Pageable pageable);

    @Query(value = "select " +
            "d.cared_date as date, count(d.id) as diaryCount from diary d " +
            "where d.member_id = :memberId and d.cared_date >= :start and d.cared_date < :end " +
            "group by d.cared_date", nativeQuery = true)
    List<MonthlyDiaryInterface> findAllByCaredDateBetween(@Param("memberId") Long memberId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
