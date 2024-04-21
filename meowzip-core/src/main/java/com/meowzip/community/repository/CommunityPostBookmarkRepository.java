package com.meowzip.community.repository;

import com.meowzip.community.entity.CommunityPost;
import com.meowzip.community.entity.CommunityPostBookmark;
import com.meowzip.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityPostBookmarkRepository extends JpaRepository<CommunityPostBookmark, Long> {

    Optional<CommunityPostBookmark> findByPostAndMember(CommunityPost post, Member member);
}
