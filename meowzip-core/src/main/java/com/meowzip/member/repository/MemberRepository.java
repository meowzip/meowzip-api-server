package com.meowzip.member.repository;

import com.meowzip.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    List<Member> findAllByNicknameContainingAndIdNot(String nickname, Long memberId, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Member m")
    long count();
}
