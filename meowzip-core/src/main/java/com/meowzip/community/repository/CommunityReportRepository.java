package com.meowzip.community.repository;

import com.meowzip.community.entity.CommunityReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityReportRepository extends JpaRepository<CommunityReport, Long> {
}
