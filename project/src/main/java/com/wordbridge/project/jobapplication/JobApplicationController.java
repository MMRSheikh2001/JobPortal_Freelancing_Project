package com.wordbridge.project.jobapplication;

import com.wordbridge.project.enums.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/jobapplications/")
@RequiredArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;


    @PostMapping
    public ResponseEntity<JobApplicationResponseDTO> apply(@RequestBody JobApplicationRequestDTO dto) {
        JobApplicationResponseDTO jobApplicationResponseDTO = jobApplicationService.apply(dto);
        return ResponseEntity.ok(jobApplicationResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponseDTO>> getAll() {
        List<JobApplicationResponseDTO> list = jobApplicationService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<JobApplicationResponseDTO> findById(@PathVariable Long id) {
        JobApplicationResponseDTO jobApplicationResponseDTO = jobApplicationService.findById(id);
        return ResponseEntity.ok(jobApplicationResponseDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        jobApplicationService.delete(id);
        return ResponseEntity.ok("Job Application Deleted");
    }

    @GetMapping("userprofile/{userProfileId}")
    public List<JobApplicationResponseDTO> findByUserProfileId(@PathVariable Long userProfileId) {
        return jobApplicationService.findByUserProfileId(userProfileId);
    }

    @GetMapping("userprofile/{userProfileId}/status/{status}")
    public List<JobApplicationResponseDTO> findByUserProfileIdAndStatus(@PathVariable Long userProfileId, @PathVariable ApplicationStatus status) {
        return jobApplicationService.findByUserProfileIdAndStatus(userProfileId, status);
    }


    @PatchMapping("withdraw/{applicationId}/userprofile/{userProfileId}")
    public ResponseEntity<JobApplicationResponseDTO> withdrawApplication(@PathVariable Long applicationId, @PathVariable Long userProfileId) {
        JobApplicationResponseDTO dto = jobApplicationService.withdrawApplication(applicationId, userProfileId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("job/{jobId}")
    public List<JobApplicationResponseDTO> findByJobId(@PathVariable Long jobId) {
        return jobApplicationService.findByJobId(jobId);
    }

    @GetMapping("job/count/{jobId}")
    public Long countByJobId(@PathVariable Long jobId) {
        return jobApplicationService.countByJobId(jobId);
    }

    @GetMapping("count/job/{jobId}/status/{status}")
    public Long countByJobIdAndStatus(@PathVariable Long jobId, @PathVariable ApplicationStatus status) {
        return jobApplicationService.countByJobIdAndStatus(jobId, status);
    }

    @GetMapping("companyprofile/{companyProfileId}")
    public List<JobApplicationResponseDTO> findByJobCompanyProfileId(@PathVariable Long companyProfileId) {
        return jobApplicationService.findByJobCompanyProfileId(companyProfileId);
    }

    @GetMapping("companyprofile/{companyProfileId}/status/{status}")
    public List<JobApplicationResponseDTO> findByJobCompanyProfileIdAndStatus(@PathVariable Long companyProfileId, @PathVariable ApplicationStatus status) {
        return jobApplicationService.findByJobCompanyProfileIdAndStatus(companyProfileId, status);
    }

    @PatchMapping("shortlist/{applicationId}")
    public ResponseEntity<JobApplicationResponseDTO> shortlistApplication(@PathVariable Long applicationId) {

        JobApplicationResponseDTO dto = jobApplicationService.shortlistApplication(applicationId);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("reject/{applicationId}")
    public ResponseEntity<JobApplicationResponseDTO> rejectApplication(@PathVariable Long applicationId) {
        JobApplicationResponseDTO dto = jobApplicationService.rejectApplication(applicationId);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("hire/{applicationId}")
    public ResponseEntity<JobApplicationResponseDTO> hireApplication(@PathVariable Long applicationId) {
        JobApplicationResponseDTO dto = jobApplicationService.hireApplication(applicationId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("companyprofile/applied/{companyProfileId}")
    public List<JobApplicationResponseDTO> findAppliedApplications(@PathVariable Long companyProfileId) {
        return jobApplicationService.findAppliedApplications(companyProfileId);
    }

    @GetMapping("companyprofile/shortlisted/{companyProfileId}")
    public List<JobApplicationResponseDTO> findCompanyShortlistedApplications(@PathVariable Long companyProfileId) {
        return jobApplicationService.findCompanyShortlistedApplications(companyProfileId);
    }

    @GetMapping("companyprofile/hired/{companyProfileId}")
    public List<JobApplicationResponseDTO> findHiredApplications(@PathVariable Long companyProfileId) {
        return jobApplicationService.findHiredApplications(companyProfileId);
    }

    @PatchMapping("companynotes/{applicationId}")
    public JobApplicationResponseDTO updateCompanyNotes(@PathVariable Long applicationId, @RequestParam String companyNotes) {
        return jobApplicationService.updateCompanyNotes(applicationId, companyNotes);
    }

    @GetMapping("pendingai")
    public List<JobApplicationResponseDTO> findPendingAIApplications() {
        return jobApplicationService.findPendingAIApplications();
    }


    @GetMapping("job/{jobId}/aicompleted")
    public List<JobApplicationResponseDTO> findCompletedAIApplications(@PathVariable Long jobId) {
        return jobApplicationService.findCompletedAIApplications(jobId);
    }

    @PostMapping("job/{jobId}/select-top-qualified")
    public ResponseEntity<String> runAIShortlisting(@PathVariable Long jobId) {

        jobApplicationService.selectTopQualifiedCandidates(jobId);
        return ResponseEntity.ok("AI shortlisting done");
    }

    @GetMapping("appliedat/desc")
    public List<JobApplicationResponseDTO> findTop20ByOrderByAppliedAtDesc() {
        return jobApplicationService.findTop20ByOrderByAppliedAtDesc();
    }

    @GetMapping("count/userprofile/{userProfileId}")
    public Long countByUserProfileId(@PathVariable Long userProfileId) {
        return jobApplicationService.countByUserProfileId(userProfileId);
    }

    @GetMapping("count/companyprofile/{companyProfileId}")
    public Long countByJobCompanyProfileId(@PathVariable Long companyProfileId) {
        return jobApplicationService.countByJobCompanyProfileId(companyProfileId);
    }

    @GetMapping("count/companyprofile/{companyProfileId}/status/{status}")
    public Long countByJobCompanyProfileIdAndStatus(@PathVariable Long companyProfileId, @PathVariable ApplicationStatus status) {
        return jobApplicationService.countByJobCompanyProfileIdAndStatus(companyProfileId, status);
    }

    @GetMapping("job/{jobId}/status/{status}")
    public List<JobApplicationResponseDTO> findByJobIdAndStatus(@PathVariable Long jobId, @PathVariable ApplicationStatus status) {
        return jobApplicationService.findByJobIdAndStatus(jobId, status);
    }

    @GetMapping("companyprofile/{companyProfileId}/appliedat/desc")
    public List<JobApplicationResponseDTO> findByJobCompanyProfileIdOrderByAppliedAtDesc(@PathVariable Long companyProfileId) {
        return jobApplicationService.findByJobCompanyProfileIdOrderByAppliedAtDesc(companyProfileId);
    }

    @GetMapping("job/{jobId}/appliedat/desc")
    public List<JobApplicationResponseDTO> findByJobIdOrderByAppliedAtDesc(@PathVariable Long jobId) {
        return jobApplicationService.findByJobIdOrderByAppliedAtDesc(jobId);
    }

    @GetMapping("userprofile/{userProfileId}/appliedat/desc")
    public List<JobApplicationResponseDTO> findByUserProfileIdOrderByAppliedAtDesc(@PathVariable Long userProfileId) {
        return jobApplicationService.findByUserProfileIdOrderByAppliedAtDesc(userProfileId);
    }

    @GetMapping("job/{jobId}/status-not/{status}")
    public Long countByJobIdAndStatusNot(@PathVariable Long jobId, @PathVariable ApplicationStatus status) {
        return jobApplicationService.countByJobIdAndStatusNot(jobId, status);
    }

    @GetMapping("expired")
    public List<JobApplicationResponseDTO> findExpiredAIApplications() {
        return jobApplicationService.findExpiredAIApplications();
    }

    @GetMapping("job/{jobId}/shortlisted")
    public List<JobApplicationResponseDTO> findAIShortlistedApplications(@PathVariable Long jobId) {
        return jobApplicationService.findAIShortlistedApplications(jobId);
    }

    @GetMapping("job/{jobId}/count/aishortlisted")
    public Long countAIShortlistedApplications(@PathVariable Long jobId) {
        return jobApplicationService.countAIShortlistedApplications(jobId);
    }

    //Prevent Duplicate
    @GetMapping("exist/job/{jobId}/userprofile/{userProfileId}")
    public Boolean existsByUserProfileIdAndJobId(@PathVariable Long userProfileId, @PathVariable Long jobId) {
        return jobApplicationService.existsByUserProfileIdAndJobId(userProfileId, jobId);
    }

    //Find user Applied specific job
    @GetMapping("job/{jobId}/userprofile/{userProfileId}")
    public ResponseEntity<JobApplicationResponseDTO> findByJobIdAndUserProfileId(
            @PathVariable Long jobId,
            @PathVariable Long userProfileId
    ) {
        return ResponseEntity.ok(jobApplicationService.findByJobIdAndUserProfileId(jobId, userProfileId));
    }


    @GetMapping("job/{jobId}/companyprofile/{companyProfileId}")
    List<JobApplicationResponseDTO> findByJobCompanyProfileIdAndJobId(@PathVariable Long companyProfileId, @PathVariable Long jobId) {
        return jobApplicationService.findByJobCompanyProfileIdAndJobId(companyProfileId, jobId);
    }


    @PostMapping("search")
    public List<JobApplicationResponseDTO> search(
            @RequestBody JobApplicationFilterRequestDTO dto
    ) {
        return jobApplicationService.search(dto);
    }

}
