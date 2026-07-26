package com.wordbridge.project.jobapplication;


import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.job.Job;
import com.wordbridge.project.job.JobRepository;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class JobApplicationMapper {
    private final UserProfileRepository userProfileRepository;
    private final JobRepository jobRepository;

    public JobApplicationResponseDTO toDTO(JobApplication j) {
        JobApplicationResponseDTO dto = new JobApplicationResponseDTO();
        dto.setId(j.getId());
        dto.setStatus(j.getStatus());
        dto.setAppliedAt(j.getAppliedAt());

        if (j.getAiDeadlineDate() != null) {
            dto.setAiDeadlineDate(j.getAiDeadlineDate());
        }

        if (j.getCompanyNotes() != null) {
            dto.setCompanyNotes(j.getCompanyNotes());
        }
        dto.setJobId(j.getJob().getId());

        dto.setJobTitle(j.getJob().getTitle());
        dto.setJobDescription(j.getJob().getJobDescription());
        dto.setCompanyProfileId(j.getJob().getCompanyProfile().getId());
        dto.setCompanyName(j.getJob().getCompanyProfile().getName());
        dto.setCompanyUserId(j.getJob().getCompanyProfile().getUser().getId());
        dto.setCompanyUserEmail(j.getJob().getCompanyProfile().getUser().getEmail());

        if (j.getJob().getCompanyProfile().getImage() != null) {
            dto.setCompanyLogo(j.getJob().getCompanyProfile().getImage());
        }

        dto.setUserProfileId(j.getUserProfile().getId());
        dto.setUserName(j.getUserProfile().getName());
        dto.setUserImage(j.getUserProfile().getImage());
        dto.setUserId(j.getUserProfile().getUser().getId());
        dto.setUserEmail(j.getUserProfile().getUser().getEmail());


        if (j.getAiMatchScore() != null) {
            dto.setAiMatchScore(j.getAiMatchScore());
        }
        if (j.getAiMatchFeedback() != null) {
            dto.setAiMatchFeedback(j.getAiMatchFeedback());
        }
        if (j.getAiInterviewScore() != null) {
            dto.setAiInterviewScore(j.getAiInterviewScore());
        }
        if (j.getAiFinalScore() != null) {
            dto.setAiFinalScore(j.getAiFinalScore());
        }
        if (j.getAiInterviewCompleted() != null) {
            dto.setAiInterviewCompleted(j.getAiInterviewCompleted());
        }
        if (j.getAiCompletedAt() != null) {
            dto.setAiCompletedAt(j.getAiCompletedAt());
        }
        if (j.getAiShortlisted() != null) {
            dto.setAiShortlisted(j.getAiShortlisted());
        }

        if (j.getJob().getAiScreeningEnabled() != null) {
            dto.setAiScreeningEnabled(j.getJob().getAiScreeningEnabled());
        }
        if (j.getJob().getAiCvScreeningEnabled() != null) {
            dto.setAiCvScreeningEnabled(j.getJob().getAiCvScreeningEnabled());
        }
        if (j.getJob().getAiInterviewEnabled() != null) {
            dto.setAiInterviewEnabled(j.getJob().getAiInterviewEnabled());
        }
        if (j.getJob().getAiMatchThreshold() != null) {
            dto.setAiMatchThreshold(j.getJob().getAiMatchThreshold());
        }
        if (j.getJob().getAiQuestionCount() != null) {
            dto.setAiQuestionCount(j.getJob().getAiQuestionCount());
        }

        return dto;
    }


    public JobApplication toEntity(JobApplicationRequestDTO dto) {
        JobApplication j = new JobApplication();


        j.setAppliedAt(LocalDateTime.now());

        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new RuntimeException("No Job Application found"));
        j.setJob(job);
        UserProfile userProfile = userProfileRepository.findById(dto.getUserProfileId())
                .orElseThrow(() -> new RuntimeException("No User Profile found"));

        j.setUserProfile(userProfile);
        if (Boolean.TRUE.equals(job.getAiScreeningEnabled())
                && Boolean.TRUE.equals(job.getAiInterviewEnabled())) {

            j.setAiDeadlineDate(
                    LocalDateTime.now().plusDays(job.getAiDeadlineDays())
            );
        }


        return j;

    }


}
