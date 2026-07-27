package com.wordbridge.project.jobapplication;

import com.wordbridge.project.dto.responsedto.CompanyProfileResponseDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.ApplicationStatus;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.job.JobResponseDTO;
import com.wordbridge.project.job.JobService;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.CompanyProfileService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/jobapplications/")
@RequiredArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    private final UserProfileService userProfileService;
    private final CompanyProfileService companyProfileService;
    private final JobService jobService;
    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<JobApplicationResponseDTO> apply(@RequestBody JobApplicationRequestDTO dto) {
        checkApplicantProfileOwnership(dto.getUserProfileId());
        JobApplicationResponseDTO jobApplicationResponseDTO = jobApplicationService.apply(dto);
        return ResponseEntity.ok(jobApplicationResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<JobApplicationResponseDTO>> getAll() {
        List<JobApplicationResponseDTO> list = jobApplicationService.getAll();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<JobApplicationResponseDTO> findById(@PathVariable Long id) {
        checkApplicationAccess(id);
        JobApplicationResponseDTO jobApplicationResponseDTO = jobApplicationService.findById(id);
        return ResponseEntity.ok(jobApplicationResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        jobApplicationService.delete(id);
        return ResponseEntity.ok("Job Application Deleted");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("userprofile/{userProfileId}")
    public List<JobApplicationResponseDTO> findByUserProfileId(@PathVariable Long userProfileId) {
        checkApplicantProfileOwnership(userProfileId);
        return jobApplicationService.findByUserProfileId(userProfileId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("userprofile/{userProfileId}/status/{status}")
    public List<JobApplicationResponseDTO> findByUserProfileIdAndStatus(@PathVariable Long userProfileId, @PathVariable ApplicationStatus status) {
        checkApplicantProfileOwnership(userProfileId);
        return jobApplicationService.findByUserProfileIdAndStatus(userProfileId, status);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("withdraw/{applicationId}/userprofile/{userProfileId}")
    public ResponseEntity<JobApplicationResponseDTO> withdrawApplication(@PathVariable Long applicationId, @PathVariable Long userProfileId) {
        checkApplicantProfileOwnership(userProfileId);
        JobApplicationResponseDTO dto = jobApplicationService.withdrawApplication(applicationId, userProfileId);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("job/{jobId}")
    public List<JobApplicationResponseDTO> findByJobId(@PathVariable Long jobId) {
        checkJobOwnershipOrAdmin(jobId);
        return jobApplicationService.findByJobId(jobId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("job/count/{jobId}")
    public Long countByJobId(@PathVariable Long jobId) {
        checkJobOwnershipOrAdmin(jobId);
        return jobApplicationService.countByJobId(jobId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("count/job/{jobId}/status/{status}")
    public Long countByJobIdAndStatus(@PathVariable Long jobId, @PathVariable ApplicationStatus status) {
        checkJobOwnershipOrAdmin(jobId);
        return jobApplicationService.countByJobIdAndStatus(jobId, status);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("companyprofile/{companyProfileId}")
    public List<JobApplicationResponseDTO> findByJobCompanyProfileId(@PathVariable Long companyProfileId) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobApplicationService.findByJobCompanyProfileId(companyProfileId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("companyprofile/{companyProfileId}/status/{status}")
    public List<JobApplicationResponseDTO> findByJobCompanyProfileIdAndStatus(@PathVariable Long companyProfileId, @PathVariable ApplicationStatus status) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobApplicationService.findByJobCompanyProfileIdAndStatus(companyProfileId, status);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @PatchMapping("shortlist/{applicationId}")
    public ResponseEntity<JobApplicationResponseDTO> shortlistApplication(@PathVariable Long applicationId) {
        checkApplicationCompanyOwnershipOrAdmin(applicationId);
        JobApplicationResponseDTO dto = jobApplicationService.shortlistApplication(applicationId);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @PatchMapping("reject/{applicationId}")
    public ResponseEntity<JobApplicationResponseDTO> rejectApplication(@PathVariable Long applicationId) {
        checkApplicationCompanyOwnershipOrAdmin(applicationId);
        JobApplicationResponseDTO dto = jobApplicationService.rejectApplication(applicationId);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @PatchMapping("hire/{applicationId}")
    public ResponseEntity<JobApplicationResponseDTO> hireApplication(@PathVariable Long applicationId) {
        checkApplicationCompanyOwnershipOrAdmin(applicationId);
        JobApplicationResponseDTO dto = jobApplicationService.hireApplication(applicationId);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("companyprofile/applied/{companyProfileId}")
    public List<JobApplicationResponseDTO> findAppliedApplications(@PathVariable Long companyProfileId) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobApplicationService.findAppliedApplications(companyProfileId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("companyprofile/shortlisted/{companyProfileId}")
    public List<JobApplicationResponseDTO> findCompanyShortlistedApplications(@PathVariable Long companyProfileId) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobApplicationService.findCompanyShortlistedApplications(companyProfileId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("companyprofile/hired/{companyProfileId}")
    public List<JobApplicationResponseDTO> findHiredApplications(@PathVariable Long companyProfileId) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobApplicationService.findHiredApplications(companyProfileId);
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PatchMapping("companynotes/{applicationId}")
    public JobApplicationResponseDTO updateCompanyNotes(@PathVariable Long applicationId, @RequestParam String companyNotes) {
        checkApplicationCompanyOwnershipOrAdmin(applicationId);
        return jobApplicationService.updateCompanyNotes(applicationId, companyNotes);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("pendingai")
    public List<JobApplicationResponseDTO> findPendingAIApplications() {
        return jobApplicationService.findPendingAIApplications();
    }


    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("job/{jobId}/aicompleted")
    public List<JobApplicationResponseDTO> findCompletedAIApplications(@PathVariable Long jobId) {
        checkJobOwnershipOrAdmin(jobId);
        return jobApplicationService.findCompletedAIApplications(jobId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @PostMapping("job/{jobId}/select-top-qualified")
    public ResponseEntity<String> runAIShortlisting(@PathVariable Long jobId) {
        checkJobOwnershipOrAdmin(jobId);

        jobApplicationService.selectTopQualifiedCandidates(jobId);
        return ResponseEntity.ok("AI shortlisting done");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("appliedat/desc")
    public List<JobApplicationResponseDTO> findTop20ByOrderByAppliedAtDesc() {
        return jobApplicationService.findTop20ByOrderByAppliedAtDesc();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("count/userprofile/{userProfileId}")
    public Long countByUserProfileId(@PathVariable Long userProfileId) {
        checkApplicantProfileOwnership(userProfileId);
        return jobApplicationService.countByUserProfileId(userProfileId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("count/companyprofile/{companyProfileId}")
    public Long countByJobCompanyProfileId(@PathVariable Long companyProfileId) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobApplicationService.countByJobCompanyProfileId(companyProfileId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("count/companyprofile/{companyProfileId}/status/{status}")
    public Long countByJobCompanyProfileIdAndStatus(@PathVariable Long companyProfileId, @PathVariable ApplicationStatus status) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobApplicationService.countByJobCompanyProfileIdAndStatus(companyProfileId, status);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("job/{jobId}/status/{status}")
    public List<JobApplicationResponseDTO> findByJobIdAndStatus(@PathVariable Long jobId, @PathVariable ApplicationStatus status) {
        checkJobOwnershipOrAdmin(jobId);
        return jobApplicationService.findByJobIdAndStatus(jobId, status);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("companyprofile/{companyProfileId}/appliedat/desc")
    public List<JobApplicationResponseDTO> findByJobCompanyProfileIdOrderByAppliedAtDesc(@PathVariable Long companyProfileId) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobApplicationService.findByJobCompanyProfileIdOrderByAppliedAtDesc(companyProfileId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("job/{jobId}/appliedat/desc")
    public List<JobApplicationResponseDTO> findByJobIdOrderByAppliedAtDesc(@PathVariable Long jobId) {
        checkJobOwnershipOrAdmin(jobId);
        return jobApplicationService.findByJobIdOrderByAppliedAtDesc(jobId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("userprofile/{userProfileId}/appliedat/desc")
    public List<JobApplicationResponseDTO> findByUserProfileIdOrderByAppliedAtDesc(@PathVariable Long userProfileId) {
        checkApplicantProfileOwnership(userProfileId);
        return jobApplicationService.findByUserProfileIdOrderByAppliedAtDesc(userProfileId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("job/{jobId}/status-not/{status}")
    public Long countByJobIdAndStatusNot(@PathVariable Long jobId, @PathVariable ApplicationStatus status) {
        checkJobOwnershipOrAdmin(jobId);
        return jobApplicationService.countByJobIdAndStatusNot(jobId, status);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("expired")
    public List<JobApplicationResponseDTO> findExpiredAIApplications() {
        return jobApplicationService.findExpiredAIApplications();
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("job/{jobId}/shortlisted")
    public List<JobApplicationResponseDTO> findAIShortlistedApplications(@PathVariable Long jobId) {
        checkJobOwnershipOrAdmin(jobId);
        return jobApplicationService.findAIShortlistedApplications(jobId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("job/{jobId}/count/aishortlisted")
    public Long countAIShortlistedApplications(@PathVariable Long jobId) {
        checkJobOwnershipOrAdmin(jobId);
        return jobApplicationService.countAIShortlistedApplications(jobId);
    }

    //Prevent Duplicate
    @PreAuthorize("hasRole('USER')")
    @GetMapping("exist/job/{jobId}/userprofile/{userProfileId}")
    public Boolean existsByUserProfileIdAndJobId(@PathVariable Long userProfileId, @PathVariable Long jobId) {
        checkApplicantProfileOwnership(userProfileId);
        return jobApplicationService.existsByUserProfileIdAndJobId(userProfileId, jobId);
    }

    //Find user Applied specific job
    @PreAuthorize("hasRole('USER')")
    @GetMapping("job/{jobId}/userprofile/{userProfileId}")
    public ResponseEntity<JobApplicationResponseDTO> findByJobIdAndUserProfileId(
            @PathVariable Long jobId,
            @PathVariable Long userProfileId
    ) {
        checkApplicantProfileOwnership(userProfileId);
        return ResponseEntity.ok(jobApplicationService.findByJobIdAndUserProfileId(jobId, userProfileId));
    }


    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("job/{jobId}/companyprofile/{companyProfileId}")
    List<JobApplicationResponseDTO> findByJobCompanyProfileIdAndJobId(@PathVariable Long companyProfileId, @PathVariable Long jobId) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobApplicationService.findByJobCompanyProfileIdAndJobId(companyProfileId, jobId);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("search")
    public List<JobApplicationResponseDTO> search(
            @RequestBody JobApplicationFilterRequestDTO dto
    ) {
        return jobApplicationService.search(dto);
    }


    // helper methods

    private void checkApplicantProfileOwnership(Long userProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        UserProfileResponseDTO profile = userProfileService.findById(userProfileId);
        if (!profile.getUserId().equals(currentUser.getId()) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkJobOwnershipOrAdmin(Long jobId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        JobResponseDTO job = jobService.findById(jobId);
        if (!job.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkCompanyOwnershipOrAdmin(Long companyProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        CompanyProfileResponseDTO company = companyProfileService.findById(companyProfileId);
        if (!company.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    // findById: applicant OR company OR admin
    private void checkApplicationAccess(Long applicationId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        JobApplicationResponseDTO app = jobApplicationService.findById(applicationId);
        boolean isApplicant = app.getUserId().equals(currentUser.getId());
        boolean isHiringCompany = app.getCompanyUserId().equals(currentUser.getId());
        if (!isApplicant && !isHiringCompany) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    // delete: applicant only (or admin)
    private void checkApplicationApplicantOwnership(Long applicationId) {
        User currentUser = authenticationService.getCurrentUser();
        JobApplicationResponseDTO app = jobApplicationService.findById(applicationId);
        if (!app.getUserId().equals(currentUser.getId()) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    // shortlist/reject/hire/companynotes: hiring company only (or admin)
    private void checkApplicationCompanyOwnershipOrAdmin(Long applicationId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        JobApplicationResponseDTO app = jobApplicationService.findById(applicationId);
        if (!app.getCompanyUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }


}
