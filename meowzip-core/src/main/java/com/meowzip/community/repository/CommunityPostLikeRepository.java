package com.meowzip.community.repository;

import com.meowzip.community.entity.CommunityPost;
import com.meowzip.community.entity.CommunityPostLike;
import com.meowzip.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityPostLikeRepository extends JpaRepository<CommunityPostLike, Long> {

    Optional<CommunityPostLike> findByPostAndMember(CommunityPost post, Member member);
}
