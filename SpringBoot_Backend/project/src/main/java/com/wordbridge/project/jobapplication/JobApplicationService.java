package com.wordbridge.project.jobapplication;

import com.wordbridge.project.enums.ApplicationStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobApplicationService {
    JobApplicationResponseDTO apply(JobApplicationRequestDTO dto);

    JobApplicationResponseDTO findById(Long id);

    List<JobApplicationResponseDTO> getAll();


    void delete(Long id);


    //user related
    List<JobApplicationResponseDTO> findByUserProfileId(Long userProfileId);

    List<JobApplicationResponseDTO> findByUserProfileIdAndStatus(
            Long userProfileId,
            ApplicationStatus status
    );



    JobApplicationResponseDTO withdrawApplication(
            Long applicationId,
            Long userProfileId
    );

    // job related
    List<JobApplicationResponseDTO> findByJobId(Long jobId);

    Long countByJobId(Long jobId);

    Long countByJobIdAndStatus(
            Long jobId,
            ApplicationStatus status
    );

    //company related
    List<JobApplicationResponseDTO> findByJobCompanyProfileId(
            Long companyProfileId
    );

    List<JobApplicationResponseDTO> findByJobCompanyProfileIdAndStatus(
            Long companyProfileId,
            ApplicationStatus status
    );

    JobApplicationResponseDTO shortlistApplication(
            Long applicationId
    );

    JobApplicationResponseDTO rejectApplication(
            Long applicationId
    );

    JobApplicationResponseDTO hireApplication(
            Long applicationId
    );

    // All applicants waiting for review
    List<JobApplicationResponseDTO> findAppliedApplications(
            Long companyProfileId
    );

    // All company shortlisted applicants
    List<JobApplicationResponseDTO> findCompanyShortlistedApplications(
            Long companyProfileId
    );

    // All hired applicants
    List<JobApplicationResponseDTO> findHiredApplications(
            Long companyProfileId
    );

    JobApplicationResponseDTO updateCompanyNotes(
            Long applicationId,
            String companyNotes
    );

    //AI Side

    List<JobApplicationResponseDTO> findPendingAIApplications();



    List<JobApplicationResponseDTO> findCompletedAIApplications(
            Long jobId
    );

    void selectTopQualifiedCandidates(Long jobId);


    //Recent Applications -----for admin dashboard
    List<JobApplicationResponseDTO> findTop20ByOrderByAppliedAtDesc();

    //Count
    Long countByUserProfileId(Long userProfileId);

    Long countByJobCompanyProfileId(Long companyProfileId);

    Long countByJobCompanyProfileIdAndStatus(
            Long companyProfileId,
            ApplicationStatus status
    );

    //Search by Job + Status
    List<JobApplicationResponseDTO>
    findByJobIdAndStatus(
            Long jobId,
            ApplicationStatus status
    );


    // Company dashboard
    List<JobApplicationResponseDTO> findByJobCompanyProfileIdOrderByAppliedAtDesc(
            Long companyProfileId
    );

    // Applications for a specific job ordered by newest
    List<JobApplicationResponseDTO> findByJobIdOrderByAppliedAtDesc(
            Long jobId
    );

    // User dashboard
    List<JobApplicationResponseDTO> findByUserProfileIdOrderByAppliedAtDesc(
            Long userProfileId
    );


    // Count applicants for a job for the status they are not
    Long countByJobIdAndStatusNot(
            Long jobId,
            ApplicationStatus status
    );

    // Applications whose AI deadline expired
    List<JobApplicationResponseDTO> findExpiredAIApplications();

    // Applications already AI shortlisted
    List<JobApplicationResponseDTO> findAIShortlistedApplications(
            Long jobId
    );

    // Count AI shortlisted
    Long countAIShortlistedApplications(
            Long jobId
    );

    //Prevent Duplicate application
    Boolean existsByUserProfileIdAndJobId(Long userProfileId,Long jobId);

    //Find User specific job
    JobApplicationResponseDTO findByJobIdAndUserProfileId(
            Long jobId,
            Long userProfileId
    );

    List<JobApplicationResponseDTO> findByJobCompanyProfileIdAndJobId(Long companyProfileId,Long jobId);

    List<JobApplicationResponseDTO> getRecentApplicationsByUserId(Long userId);

    Long countAllHiredCandidates();

    List<JobApplicationResponseDTO> search(JobApplicationFilterRequestDTO dto);


}
