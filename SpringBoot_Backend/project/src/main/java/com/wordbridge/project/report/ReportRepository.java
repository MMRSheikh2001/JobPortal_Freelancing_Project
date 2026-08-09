package com.wordbridge.project.report;

import com.wordbridge.project.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long>, JpaSpecificationExecutor<Report> {

    List<Report> findByUserId(Long userId);

    List<Report> findByStatus(ReportStatus reportStatus);

    Long countByStatus(ReportStatus reportStatus);




}
