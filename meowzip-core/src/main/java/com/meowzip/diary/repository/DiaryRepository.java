package com.meowzip.diary.repository;

import com.meowzip.diary.entity.Diary;
import com.meowzip.diary.entity.MonthlyDiaryInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query(value = "SELECT DISTINCT d.* FROM diary d " +
            "LEFT JOIN tagged_cat tc ON d.id = tc.diary_id " +
            "WHERE d.member_id = :memberId AND d.cared_date = :caredDate " +
            "AND (:catId IS NULL OR tc.cat_id = :catId)",
            countQuery = "SELECT COUNT(DISTINCT d.id) FROM diary d " +
                    "LEFT JOIN tagged_cat tc ON d.id = tc.diary_id " +
                    "WHERE d.member_id = :memberId AND d.cared_date = :caredDate " +
                    "AND (:catId IS NULL OR tc.cat_id = :catId)",
            nativeQuery = true)
    Page<Diary> findDiariesByMemberAndCaredDateAndOptionalCatId(
            @Param("memberId") Long memberId,
            @Param("caredDate") LocalDate caredDate,
            @Param("catId") Long catId,
            Pageable pageable);


    @Query(value = "select " +
            "d.cared_date as date, count(d.id) as diaryCount from diary d " +
            "where d.member_id = :memberId and d.cared_date >= :start and d.cared_date < :end " +
            "group by d.cared_date", nativeQuery = true)
    List<MonthlyDiaryInterface> findAllByCaredDateBetween(@Param("memberId") Long memberId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
