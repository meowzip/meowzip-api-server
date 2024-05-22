package com.meowzip.apiserver.community.service;

import com.meowzip.community.entity.CommunityReport;
import com.meowzip.community.entity.TargetType;
import com.meowzip.community.repository.CommunityReportRepository;
import com.meowzip.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommunityReportService {

    private final CommunityReportRepository communityReportRepository;

    // TODO: 기획 쪽에 여러번 신고 가능한지 확인하기
    @Transactional
    public void report(Member reporter, TargetType targetType, Long targetId) {
        CommunityReport report = CommunityReport.builder()
                .targetId(targetId)
                .targetType(targetType)
                .member(reporter)
                .build();

        communityReportRepository.save(report);
    }
}
