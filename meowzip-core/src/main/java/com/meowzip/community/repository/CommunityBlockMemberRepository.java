package com.meowzip.community.repository;

import com.meowzip.community.entity.CommunityBlockMember;
import com.meowzip.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityBlockMemberRepository extends JpaRepository<CommunityBlockMember, Long> {
    Optional<CommunityBlockMember> findByMemberAndBlockedMember(Member member, Member blockedMember);

    List<CommunityBlockMember> findAllByMember(Member member);
}
