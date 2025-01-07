package com.meowzip.community.repository;

import com.meowzip.community.entity.CommunityPost;
import com.meowzip.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    @Query("SELECT p FROM CommunityPost p " +
            "WHERE NOT EXISTS (" +
            "    SELECT 1 FROM CommunityBlockMember b " +
            "    WHERE b.member = :member AND b.blockedMember = p.member" +
            ")")
    Page<CommunityPost> findAllFilteredByBlockedMembers(
            @Param("member") Member member,
            Pageable pageable);

    List<CommunityPost> findAllByMemberOrderByCreatedAtDesc(Member member, PageRequest pageRequest);

    int countByMember(Member member);

    @Query("SELECT b.post FROM CommunityPostBookmark b WHERE b.member.id = :memberId")
    List<CommunityPost> findAllByMemberAndIsBookmarked(@Param("memberId") Long memberId, PageRequest pageRequest);
}
