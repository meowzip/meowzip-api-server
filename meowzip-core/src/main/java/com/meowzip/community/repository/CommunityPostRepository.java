package com.meowzip.community.repository;

import com.meowzip.community.entity.CommunityPost;
import com.meowzip.member.entity.Member;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    List<CommunityPost> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);

    List<CommunityPost> findAllByMemberOrderByCreatedAtDesc(Member member, PageRequest pageRequest);

    int countByMember(Member member);
}
