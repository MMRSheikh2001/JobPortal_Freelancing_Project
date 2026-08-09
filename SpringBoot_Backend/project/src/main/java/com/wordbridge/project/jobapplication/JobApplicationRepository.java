package com.wordbridge.project.jobapplication;

import com.wordbridge.project.enums.ApplicationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>,
        JpaSpecificationExecutor<JobApplication> {
    //user related
    List<JobApplication> findByUserProfileId(Long userProfileId);

    List<JobApplication> findByUserProfileIdAndStatus(
            Long userProfileId,
            ApplicationStatus status
    );

    //Prevent duplicate
    Boolean existsByUserProfileIdAndJobId(Long userProfileId, Long jobId);

    // job related
    List<JobApplication> findByJobId(Long jobId);

    Long countByJobId(Long jobId);

    Long countByJobIdAndStatus(
            Long jobId,
            ApplicationStatus status
    );

    //company related
    List<JobApplication> findByJobCompanyProfileId(
            Long companyProfileId
    );

    List<JobApplication> findByJobCompanyProfileIdAndStatus(
            Long companyProfileId,
            ApplicationStatus status
    );

    //Duplicate Apply Prevention
    boolean existsByJobIdAndUserProfileId(
            Long jobId,
            Long userProfileId
    );

    //Recent Applications -----for admin dashboard
    List<JobApplication> findTop20ByOrderByAppliedAtDesc();

    //Count
    Long countByUserProfileId(Long userProfileId);

    Long countByJobCompanyProfileId(Long companyProfileId);

    Long countByJobCompanyProfileIdAndStatus(
            Long companyProfileId,
            ApplicationStatus status
    );

    //Search by Job + Status
    List<JobApplication>
    findByJobIdAndStatus(
            Long jobId,
            ApplicationStatus status
    );

    // Check if a user already applied to a job
    Optional<JobApplication> findByJobIdAndUserProfileId(
            Long jobId,
            Long userProfileId
    );

    // Company dashboard
    List<JobApplication> findByJobCompanyProfileIdOrderByAppliedAtDesc(
            Long companyProfileId
    );

    // Applications for a specific job ordered by newest
    List<JobApplication> findByJobIdOrderByAppliedAtDesc(
            Long jobId
    );

    // User dashboard
    List<JobApplication> findByUserProfileIdOrderByAppliedAtDesc(
            Long userProfileId
    );

    // Specific application lookup
    Optional<JobApplication> findByIdAndJobCompanyProfileId(
            Long applicationId,
            Long companyProfileId
    );

    // User can only update/withdraw own application
    Optional<JobApplication> findByIdAndUserProfileId(
            Long applicationId,
            Long userProfileId
    );

    // Count applicants for a job for the status they are not
    Long countByJobIdAndStatusNot(
            Long jobId,
            ApplicationStatus status
    );

    // Applications waiting for AI interview
    List<JobApplication> findByStatus(
            ApplicationStatus status
    );

    // AI completed but not shortlisted yet
    List<JobApplication> findByJobIdAndAiInterviewCompletedTrue(
            Long jobId
    );

    // Sort by AI score
    List<JobApplication>
    findByJobIdOrderByAiFinalScoreDesc(
            Long jobId
    );

    // Already AI shortlisted
    List<JobApplication>
    findByJobIdAndAiShortlistedTrue(
            Long jobId
    );

    //Dashboard count statistics
    Long countByJobIdAndAiShortlistedTrue(
            Long jobId
    );

    List<JobApplication>
    findByStatusAndAiDeadlineDateBefore(
            ApplicationStatus status,
            LocalDateTime date
    );

    List<JobApplication>
    findByJobIdAndAiShortlistedTrueOrderByAiFinalScoreDesc(
            Long jobId
    );

    List<JobApplication> findByJobCompanyProfileIdAndJobId(Long companyProfileId, Long jobId);

    @Query("""
            SELECT ja
            FROM JobApplication ja
            WHERE ja.userProfile.user.id = :userId
            ORDER BY ja.appliedAt DESC
            """)
    List<JobApplication> findRecentApplicationsByUserId(Long userId, Pageable pageable);

Long countByStatus(ApplicationStatus status);

    @Query("""
SELECT COUNT(ja)
FROM JobApplication ja
WHERE ja.aiMatchScore IS NOT NULL
""")
    Long countApplicationsWithAIMatchScore();

}
