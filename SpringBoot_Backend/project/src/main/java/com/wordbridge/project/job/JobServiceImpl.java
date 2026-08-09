package com.wordbridge.project.job;

import com.wordbridge.project.ai.service.ResumeScreeningService;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.ApplicationStatus;
import com.wordbridge.project.enums.TransactionType;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.jobapplication.JobApplication;
import com.wordbridge.project.jobapplication.JobApplicationRepository;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.transaction.TransactionService;
import com.wordbridge.project.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final JobApplicationRepository jobApplicationRepository;
    private final ResumeScreeningService resumeScreeningService;

    @Override
    @Transactional
    public JobResponseDTO save(JobRequestDTO dto) {
        Job job = jobMapper.toEntity(dto);
        validateAISettings(dto);

        BigDecimal totalCost = calculateJobCost(job);

        User admin = userRepository.findByRole(UserRole.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        walletService.transfer(
                job.getCompanyProfile().getUser().getId(),
                admin.getId(),
                totalCost
        );



        Job savedJob = jobRepository.save(job);

        transactionService.createTransaction(
                TransactionType.JOB_POST_PAYMENT,
                job.getCompanyProfile().getUser(),
                admin,
                totalCost,
                "Payment for Job #" + savedJob.getId()
        );


        return jobMapper.toDTO(savedJob);
    }

    @Override
    public List<JobResponseDTO> getAll() {
        return jobRepository.findAll().stream().map(jobMapper::toDTO).toList();
    }

    @Override
    public JobResponseDTO findById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Job found by this id"));
        return jobMapper.toDTO(job);
    }

    @Override
    @Transactional
    public JobResponseDTO update(Long id, JobRequestDTO dto) {

        Job exist = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Job found by this id"));

        validateAISettings(dto);

        Job job = jobMapper.toEntity(dto);
        job.setId(exist.getId());
        job.setCreatedAt(exist.getCreatedAt());
        job.setUpdatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        BigDecimal totalCost = calculateJobCost(job);

        User admin = userRepository.findByRole(UserRole.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        walletService.transfer(
                job.getCompanyProfile().getUser().getId(),
                admin.getId(),
                totalCost
        );




        Job updated = jobRepository.save(job);
        synchronizeApplications(updated);

        transactionService.createTransaction(
                TransactionType.JOB_POST_PAYMENT,
                job.getCompanyProfile().getUser(),
                admin,
                totalCost,
                "Payment for Job #" + updated.getId()
        );

        return jobMapper.toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        Job exist = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Job found by this id"));
        jobRepository.delete(exist);
    }

    //AI Validate method
    private void validateAISettings(JobRequestDTO dto) {

        if (!Boolean.TRUE.equals(dto.getAiScreeningEnabled())) {
            return;
        }
        if (dto.getAiScreeningEnabled()
                && !Boolean.TRUE.equals(dto.getAiCvScreeningEnabled())
                && !Boolean.TRUE.equals(dto.getAiInterviewEnabled())) {

            throw new RuntimeException(
                    "At least one AI screening method must be enabled"
            );
        }
        if (dto.getAiMatchThreshold() == null) {
            dto.setAiMatchThreshold(60);
        }

        if (dto.getAiDeadlineDays() == null) {
            dto.setAiDeadlineDays(3);
        }

        if (Boolean.TRUE.equals(dto.getAiInterviewEnabled())) {

            if (dto.getAiQuestionCount() == null || dto.getAiQuestionCount() <= 0) {
                throw new RuntimeException("AI Question Count is required");
            }

            if (dto.getAiDeadlineDays() == null || dto.getAiDeadlineDays() <= 0) {
                throw new RuntimeException("AI Deadline Days is required");
            }
        }

        if (Boolean.TRUE.equals(dto.getAiCvScreeningEnabled())) {

            if (dto.getAiMatchThreshold() == null) {
                throw new RuntimeException("AI Match Threshold is required");
            }

            if (dto.getAiShortlistCount() == null || dto.getAiShortlistCount() <= 0) {
                throw new RuntimeException("AI Shortlist Count is required");
            }
        }
        if (dto.getAiShortlistCount() != null
                && dto.getVacancy() != null
                && dto.getAiShortlistCount() < dto.getVacancy()) {

            throw new RuntimeException(
                    "AI shortlist count cannot be less than vacancy"
            );
        }
        if (dto.getAiQuestionCount() != null
                && dto.getAiQuestionCount() > 20) {
            throw new RuntimeException(
                    "AI Question Count cannot exceed 20"
            );
        }

    }

    //Custom Jpa Repository Methods


    @Override
    public List<JobResponseDTO> findByCompanyProfileId(Long companyProfileId) {
        return jobRepository.findByCompanyProfileId(companyProfileId).stream().map(jobMapper::toDTO).toList();
    }

    @Override
    public List<JobResponseDTO> findByCompanyProfileUserId(Long userId) {
        return jobRepository.findByCompanyProfileUserId(userId).stream().map(jobMapper::toDTO).toList();
    }

    @Override
    public Long countByCompanyProfileId(Long companyProfileId) {
        return jobRepository.countByCompanyProfileId(companyProfileId);
    }

    @Override
    public void deleteByCompanyProfileId(Long companyProfileId) {
        jobRepository.deleteByCompanyProfileId(companyProfileId);
    }


    @Override
    public List<JobResponseDTO> findByIsActiveTrue() {
        return jobRepository.findByIsActiveTrue().stream().map(jobMapper::toDTO).toList();
    }


    @Override
    public List<JobResponseDTO> findByCompanyProfileIdAndIsActiveTrue(Long companyProfileId) {
        return jobRepository.findByCompanyProfileIdAndIsActiveTrue(companyProfileId).stream().map(jobMapper::toDTO).toList();
    }

    @Override
    public List<JobResponseDTO> findTop10ByIsActiveTrueOrderByCreatedAtDesc() {
        return jobRepository.findTop10ByIsActiveTrueOrderByCreatedAtDesc().stream().map(jobMapper::toDTO).toList();
    }

    @Override
    public List<JobResponseDTO> findTop20ByIsActiveTrueOrderByCreatedAtDesc() {
        return jobRepository.findTop20ByIsActiveTrueOrderByCreatedAtDesc().stream().map(jobMapper::toDTO).toList();
    }


    @Override
    public Long countByCompanyProfileIdAndIsActiveTrue(Long companyProfileId) {
        return jobRepository.countByCompanyProfileIdAndIsActiveTrue(companyProfileId);
    }

    @Override
    public Long countByCompanyProfileIdAndIsActiveFalse(Long companyProfileId) {
        return jobRepository.countByCompanyProfileIdAndIsActiveFalse(companyProfileId);
    }

    @Override
    public List<JobResponseDTO> search(
            JobSearchRequestDTO dto
    ) {

        return jobRepository.findAll(

                        JobSpecification.filter(

                                dto.getKeyword(),

                                dto.getCategoryId(),

                                dto.getCountryId(),

                                dto.getDivisionId(),

                                dto.getDistrictId(),

                                dto.getPoliceStationId(),

                                dto.getEmploymentType(),

                                dto.getWorkPlaceType(),

                                dto.getMinSalary(),

                                dto.getMaxSalary(),

                                dto.getActive()

                        )

                ).stream()

                .map(jobMapper::toDTO)

                .toList();

    }

    @Override
    public JobResponseDTO changeJobStatus(Long id) {

        Job exist = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Job found by this id"));
        if (exist.getIsActive()) {
            exist.setIsActive(false);
        } else {
            exist.setIsActive(true);
        }
        exist.setUpdatedAt(LocalDateTime.now());
        Job update = jobRepository.save(exist);

        return jobMapper.toDTO(update);
    }

    @Override
    public Long countByIsActiveTrue() {
        return jobRepository.countByIsActiveTrue();
    }

    @Override
    public Long countByIsActiveFalse() {
        return jobRepository.countByIsActiveFalse();
    }


    //Calculate cost of job posting
    private static final BigDecimal JOB_POST_PRICE = BigDecimal.valueOf(200);
    private static final BigDecimal AI_CV_SCREENING_PRICE = BigDecimal.valueOf(100);
    private static final BigDecimal AI_INTERVIEW_PRICE = BigDecimal.valueOf(200);

    private BigDecimal calculateJobCost(Job job) {

        BigDecimal totalCost = JOB_POST_PRICE;


        if (Boolean.TRUE.equals(job.getAiCvScreeningEnabled())) {
            totalCost = totalCost.add(AI_CV_SCREENING_PRICE);
        }

        if (Boolean.TRUE.equals(job.getAiInterviewEnabled())) {
            totalCost = totalCost.add(AI_INTERVIEW_PRICE);
        }

        return totalCost;
    }


    private void synchronizeApplications(Job job) {

        List<JobApplication> applications =
                jobApplicationRepository.findByJobId(job.getId());

        // Case 1
        // AI completely disabled
        if (!Boolean.TRUE.equals(job.getAiScreeningEnabled())) {

            for (JobApplication app : applications) {

                if (app.getStatus() == ApplicationStatus.AI_PENDING) {

                    app.setStatus(ApplicationStatus.APPLIED);
                    app.setAiDeadlineDate(null);
                    app.setAiInterviewCompleted(false);

                }

            }

            jobApplicationRepository.saveAll(applications);
            return;
        }

        // Case 2
        // CV Screening enabled
        if (Boolean.TRUE.equals(job.getAiCvScreeningEnabled())) {

            for (JobApplication app : applications) {

                resumeScreeningService.screenApplication(app.getId());

            }

            return;
        }

        // Case 3
        // CV Screening disabled
        // Interview enabled
        if (Boolean.TRUE.equals(job.getAiInterviewEnabled())) {

            for (JobApplication app : applications) {

                if (app.getStatus() == ApplicationStatus.APPLIED
                ) {

                    app.setStatus(ApplicationStatus.AI_PENDING);
                    app.setAiDeadlineDate(
                            LocalDateTime.now()
                                    .plusDays(job.getAiDeadlineDays())
                    );

                }

            }

            jobApplicationRepository.saveAll(applications);
        }
    }

}
