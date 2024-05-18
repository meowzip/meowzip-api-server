package com.meowzip.apiserver.community.service;

import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.community.entity.CommunityBlockMember;
import com.meowzip.community.entity.CommunityPost;
import com.meowzip.community.repository.CommunityBlockMemberRepository;
import com.meowzip.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityBlockMemberService {

    private final CommunityBlockMemberRepository communityBlockMemberRepository;

    @Transactional
    public void block(Member member, Member blockedMember) {
        communityBlockMemberRepository.findByMemberAndBlockedMember(member, blockedMember)
                .ifPresent(block -> {
                    throw new ClientException.Conflict(EnumErrorCode.ALREADY_BLOCKED);
                });

        CommunityBlockMember block = CommunityBlockMember.builder()
                .member(member)
                .blockedMember(blockedMember)
                .build();

        communityBlockMemberRepository.save(block);
    }

    public List<CommunityBlockMember> getByMember(Member member) {
        return communityBlockMemberRepository.findAllByMember(member);
    }
}
