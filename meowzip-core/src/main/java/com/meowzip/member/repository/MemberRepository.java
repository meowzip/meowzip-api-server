package com.meowzip.member.repository;

import com.meowzip.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    @Query("""
                SELECT m
                FROM Member m
                WHERE (:nickname IS NULL OR m.nickname LIKE %:nickname%)
                  AND m.id <> :memberId
            """)
    List<Member> findAllByNicknameContainingAndIdNot(@Param("nickname") String nickname, @Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Member m")
    long count();

    @Query("SELECT COUNT(m) FROM Member m WHERE (:nickname IS NULL OR m.nickname LIKE %:nickname%) AND m.id <> :id")
    int countByNicknameContainingAndIdNot(@Param("nickname") String nickname, @Param("id") Long id);
}
