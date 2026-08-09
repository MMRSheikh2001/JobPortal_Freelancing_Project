package com.wordbridge.project.jobapplication;

import com.wordbridge.project.ai.service.ResumeScreeningService;
import com.wordbridge.project.enums.ApplicationStatus;
import com.wordbridge.project.notification.NotificationService;
import com.wordbridge.project.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationMapper jobApplicationMapper;
    private final ResumeScreeningService resumeScreeningService;
    private final NotificationService notificationService;


    @Override
    @Transactional
    public JobApplicationResponseDTO apply(JobApplicationRequestDTO dto) {
        if (jobApplicationRepository.existsByJobIdAndUserProfileId(
                dto.getJobId(),
                dto.getUserProfileId())) {
            throw new RuntimeException("Already applied.");
        }
        JobApplication jobApplication = jobApplicationMapper.toEntity(dto);

        jobApplication.setStatus(ApplicationStatus.APPLIED);

        jobApplication.setCompanyNotes("");

        jobApplication.setAiInterviewCompleted(false);

        jobApplication.setAiShortlisted(false);

        JobApplication saved = jobApplicationRepository.save(jobApplication);

        //AI Resume Screening
        if (Boolean.TRUE.equals(saved.getJob().getAiScreeningEnabled())
                && Boolean.TRUE.equals(saved.getJob().getAiCvScreeningEnabled())) {

            resumeScreeningService.screenApplication(saved.getId());
        }

        notificationService.createNotification(
                saved.getJob().getCompanyProfile().getUser().getId(),
                "New Job Application",
                saved.getUserProfile().getName() + " applied for your job.",
                NotificationType.JOB_APPLIED,
                saved.getId()
        );


        return jobApplicationMapper.toDTO(saved);
    }

    @Override
    public JobApplicationResponseDTO findById(Long id) {
        JobApplication jobApplication = jobApplicationRepository.findById(id).orElseThrow(() -> new RuntimeException("No Job Application found"));
        return jobApplicationMapper.toDTO(jobApplication);
    }

    @Override
    public List<JobApplicationResponseDTO> getAll() {
        return jobApplicationRepository.findAll().stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public void delete(Long id) {
        JobApplication jobApplication = jobApplicationRepository.findById(id).orElseThrow(() -> new RuntimeException("No Job Application found"));
        jobApplicationRepository.delete(jobApplication);
    }

    @Override
    public List<JobApplicationResponseDTO> findByUserProfileId(Long userProfileId) {
        return jobApplicationRepository.findByUserProfileId(userProfileId).stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public List<JobApplicationResponseDTO> findByUserProfileIdAndStatus(Long userProfileId, ApplicationStatus status) {
        return jobApplicationRepository.findByUserProfileIdAndStatus(userProfileId, status).stream().map(jobApplicationMapper::toDTO).toList();
    }


    @Override
    public JobApplicationResponseDTO withdrawApplication(Long applicationId, Long userProfileId) {
        JobApplication jobApplication = jobApplicationRepository.findByIdAndUserProfileId(applicationId, userProfileId)
                .orElseThrow(() -> new RuntimeException("No Job Application found by this User Profile id and Job Application id"));
        if (jobApplication.getStatus() == ApplicationStatus.HIRED) {
            throw new RuntimeException("Already hired.");
        }
        jobApplication.setStatus(ApplicationStatus.WITHDRAWN);

        return jobApplicationMapper.toDTO(jobApplicationRepository.save(jobApplication));
    }

    @Override
    public List<JobApplicationResponseDTO> findByJobId(Long jobId) {
        return jobApplicationRepository.findByJobId(jobId).stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public Long countByJobId(Long jobId) {
        return jobApplicationRepository.countByJobId(jobId);
    }

    @Override
    public Long countByJobIdAndStatus(Long jobId, ApplicationStatus status) {
        return jobApplicationRepository.countByJobIdAndStatus(jobId, status);
    }

    @Override
    public List<JobApplicationResponseDTO> findByJobCompanyProfileId(Long companyProfileId) {
        return jobApplicationRepository.findByJobCompanyProfileId(companyProfileId).stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public List<JobApplicationResponseDTO> findByJobCompanyProfileIdAndStatus(Long companyProfileId, ApplicationStatus status) {
        return jobApplicationRepository.findByJobCompanyProfileIdAndStatus(companyProfileId, status).stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public JobApplicationResponseDTO shortlistApplication(Long applicationId) {

        JobApplication jobApplication = jobApplicationRepository.findById(applicationId).
                orElseThrow(() -> new RuntimeException("No Job Application found"));
        if (jobApplication.getStatus() == ApplicationStatus.HIRED) {
            throw new RuntimeException("Already hired.");
        }
        jobApplication.setStatus(ApplicationStatus.COMPANY_SHORTLISTED);
        JobApplication saved = jobApplicationRepository.save(jobApplication);

        notificationService.createNotification(
                saved.getUserProfile().getUser().getId(),
                "Application Shortlisted",
                "Your application has been shortlisted.",
                NotificationType.JOB_SHORTLISTED,
                saved.getId()
        );

        return jobApplicationMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public JobApplicationResponseDTO rejectApplication(Long applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId).
                orElseThrow(() -> new RuntimeException("No Job Application found"));
        if (jobApplication.getStatus() == ApplicationStatus.HIRED) {
            throw new RuntimeException("Already hired.");
        }
        jobApplication.setStatus(ApplicationStatus.REJECTED);


        JobApplication saved = jobApplicationRepository.save(jobApplication);

        notificationService.createNotification(
                saved.getUserProfile().getUser().getId(),
                "Application Rejeced",
                "Your application has been Rejected.",
                NotificationType.JOB_REJECTED,
                saved.getId()
        );

        return jobApplicationMapper.toDTO(saved);
    }

    @Override
    public JobApplicationResponseDTO hireApplication(Long applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId).
                orElseThrow(() -> new RuntimeException("No Job Application found"));
        if (jobApplication.getStatus() == ApplicationStatus.HIRED) {
            throw new RuntimeException("Already hired.");
        }
        jobApplication.setStatus(ApplicationStatus.HIRED);

        JobApplication saved = jobApplicationRepository.save(jobApplication);

        notificationService.createNotification(
                saved.getUserProfile().getUser().getId(),
                "Application Hired",
                "Your application has been Accepted.",
                NotificationType.JOB_HIRED,
                saved.getId()
        );

        return jobApplicationMapper.toDTO(saved);
    }

    @Override
    public List<JobApplicationResponseDTO> findAppliedApplications(Long companyProfileId) {
        return jobApplicationRepository.findByJobCompanyProfileIdAndStatus(companyProfileId, ApplicationStatus.APPLIED).stream().
                map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public List<JobApplicationResponseDTO> findCompanyShortlistedApplications(Long companyProfileId) {
        return jobApplicationRepository.findByJobCompanyProfileIdAndStatus(companyProfileId, ApplicationStatus.COMPANY_SHORTLISTED).stream().
                map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public List<JobApplicationResponseDTO> findHiredApplications(Long companyProfileId) {
        return jobApplicationRepository.findByJobCompanyProfileIdAndStatus(companyProfileId, ApplicationStatus.HIRED).stream().
                map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public JobApplicationResponseDTO updateCompanyNotes(Long applicationId, String companyNotes) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("No Job Application found"));
        jobApplication.setCompanyNotes(companyNotes);
        return jobApplicationMapper.toDTO(jobApplicationRepository.save(jobApplication));
    }

    @Override
    public List<JobApplicationResponseDTO> findPendingAIApplications() {
        return jobApplicationRepository.findByStatus(ApplicationStatus.AI_PENDING)
                .stream().map(jobApplicationMapper::toDTO).toList();
    }


    @Override
    public List<JobApplicationResponseDTO> findCompletedAIApplications(Long jobId) {
        return jobApplicationRepository.findByJobIdAndStatus(jobId, ApplicationStatus.AI_COMPLETED)
                .stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public void selectTopQualifiedCandidates(Long jobId) {

        List<JobApplication> applications =
                jobApplicationRepository.findByJobIdAndStatus(
                        jobId,
                        ApplicationStatus.AUTOMATIC_QUALIFIED
                );

        if (applications.isEmpty()) {
            return;
        }

        int shortlistCount = applications.get(0)
                .getJob()
                .getAiShortlistCount();

        applications.sort((a, b) ->
                Integer.compare(
                        b.getAiFinalScore() == null ? 0 : b.getAiFinalScore(),
                        a.getAiFinalScore() == null ? 0 : a.getAiFinalScore()
                ));

        for (int i = 0; i < applications.size(); i++) {

            JobApplication application = applications.get(i);

            if (i < shortlistCount) {
                application.setAiShortlisted(true);

            } else {
                application.setAiShortlisted(false);

            }
        }

        jobApplicationRepository.saveAll(applications);
    }

    @Override
    public List<JobApplicationResponseDTO> findTop20ByOrderByAppliedAtDesc() {
        return jobApplicationRepository.findTop20ByOrderByAppliedAtDesc().stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public Long countByUserProfileId(Long userProfileId) {
        return jobApplicationRepository.countByUserProfileId(userProfileId);
    }

    @Override
    public Long countByJobCompanyProfileId(Long companyProfileId) {
        return jobApplicationRepository.countByJobCompanyProfileId(companyProfileId);
    }

    @Override
    public Long countByJobCompanyProfileIdAndStatus(Long companyProfileId, ApplicationStatus status) {
        return jobApplicationRepository.countByJobCompanyProfileIdAndStatus(companyProfileId, status);
    }

    @Override
    public List<JobApplicationResponseDTO> findByJobIdAndStatus(Long jobId, ApplicationStatus status) {
        return jobApplicationRepository.findByJobIdAndStatus(jobId, status).stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public List<JobApplicationResponseDTO> findByJobCompanyProfileIdOrderByAppliedAtDesc(Long companyProfileId) {
        return jobApplicationRepository.findByJobCompanyProfileIdOrderByAppliedAtDesc(companyProfileId)
                .stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public List<JobApplicationResponseDTO> findByJobIdOrderByAppliedAtDesc(Long jobId) {
        return jobApplicationRepository.findByJobIdOrderByAppliedAtDesc(jobId)
                .stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public List<JobApplicationResponseDTO> findByUserProfileIdOrderByAppliedAtDesc(Long userProfileId) {
        return jobApplicationRepository.findByUserProfileIdOrderByAppliedAtDesc(userProfileId)
                .stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public Long countByJobIdAndStatusNot(Long jobId, ApplicationStatus status) {
        return jobApplicationRepository.countByJobIdAndStatusNot(jobId, status);
    }

    @Override
    public List<JobApplicationResponseDTO> findExpiredAIApplications() {
        return jobApplicationRepository.findByStatusAndAiDeadlineDateBefore(
                ApplicationStatus.AI_PENDING,
                LocalDateTime.now()
        ).stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public List<JobApplicationResponseDTO> findAIShortlistedApplications(Long jobId) {
        return jobApplicationRepository.findByJobIdAndStatus(jobId, ApplicationStatus.AUTOMATIC_QUALIFIED)
                .stream().map(jobApplicationMapper::toDTO).toList();
    }

    @Override
    public Long countAIShortlistedApplications(Long jobId) {
        return jobApplicationRepository.countByJobIdAndStatus(jobId, ApplicationStatus.AUTOMATIC_QUALIFIED);
    }

    @Override
    public Boolean existsByUserProfileIdAndJobId(Long userProfileId, Long jobId) {
        return jobApplicationRepository.existsByUserProfileIdAndJobId(userProfileId, jobId);
    }

    @Override
    public JobApplicationResponseDTO findByJobIdAndUserProfileId(Long jobId, Long userProfileId) {
        JobApplication jobApplication = jobApplicationRepository.findByJobIdAndUserProfileId(jobId, userProfileId)
                .orElseThrow(() -> new RuntimeException("No Job application found"));

        return jobApplicationMapper.toDTO(jobApplication);

    }

    @Override
    public List<JobApplicationResponseDTO> findByJobCompanyProfileIdAndJobId(Long companyProfileId, Long jobId) {
        return jobApplicationRepository.findByJobCompanyProfileIdAndJobId(companyProfileId, jobId)
                .stream().map(jobApplicationMapper::toDTO).toList();


    }


    @Override
    public List<JobApplicationResponseDTO> getRecentApplicationsByUserId(Long userId) {

        Pageable pageable = PageRequest.of(0, 5);

        return jobApplicationRepository
                .findRecentApplicationsByUserId(userId, pageable)
                .stream()
                .map(jobApplicationMapper::toDTO)
                .toList();
    }

    @Override
    public Long countAllHiredCandidates() {
        return jobApplicationRepository.countByStatus(ApplicationStatus.HIRED);
    }

    @Override
    public List<JobApplicationResponseDTO> search(JobApplicationFilterRequestDTO dto) {

        Specification<JobApplication> specification =
                JobApplicationSpecification.filter(dto);

        return jobApplicationRepository.findAll(specification)
                .stream()
                .map(jobApplicationMapper::toDTO)
                .toList();
    }


}
