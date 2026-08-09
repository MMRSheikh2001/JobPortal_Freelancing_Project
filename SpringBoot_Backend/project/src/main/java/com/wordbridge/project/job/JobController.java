package com.wordbridge.project.job;


import com.wordbridge.project.dto.responsedto.CompanyProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.CompanyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/jobs/")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    private final CompanyProfileService companyProfileService;
    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('COMPANY')")
    @PostMapping
    public ResponseEntity<JobResponseDTO> save(@RequestBody JobRequestDTO dto) {
        checkCompanyOwnership(dto.getCompanyProfileId());

        JobResponseDTO saved = jobService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAll() {
        List<JobResponseDTO> list = jobService.getAll();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("{id}")
    public ResponseEntity<JobResponseDTO> getById(@PathVariable Long id) {
        JobResponseDTO dto = jobService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PutMapping("{id}")
    public ResponseEntity<JobResponseDTO> update(@RequestBody JobRequestDTO dto, @PathVariable Long id) {
        checkJobOwnership(id);

        JobResponseDTO updated = jobService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkJobOwnershipOrAdmin(id);
        jobService.delete(id);
        return ResponseEntity.ok("Job Deleted");
    }

    //Search api

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("companyprofile/{id}")
    public List<JobResponseDTO> findByUserProfileId(@PathVariable Long id) {
        checkCompanyOwnershipOrAdmin(id);
        return jobService.findByCompanyProfileId(id);
    }


    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("user/{userId}")
    public List<JobResponseDTO> findByCompanyProfileUserId(@PathVariable Long userId) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
        return jobService.findByCompanyProfileUserId(userId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("companyprofile/count/{companyProfileId}")
    public Long countByCompanyProfileId(@PathVariable Long companyProfileId) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobService.countByCompanyProfileId(companyProfileId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @DeleteMapping("companyprofile/delete/{companyProfileId}")
    public void deleteByCompanyProfileId(@PathVariable Long companyProfileId) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        jobService.deleteByCompanyProfileId(companyProfileId);
    }


    @PreAuthorize("permitAll()")
    @GetMapping("active=true")
    public List<JobResponseDTO> findByIsActiveTrue() {
        return jobService.findByIsActiveTrue();
    }


    @PreAuthorize("permitAll()")
    @GetMapping("companyprofile/{companyProfileId}/active=true")
    public List<JobResponseDTO> findByCompanyProfileIdAndIsActiveTrue(@PathVariable Long companyProfileId) {
        return jobService.findByCompanyProfileIdAndIsActiveTrue(companyProfileId);
    }

    //free api
    @PreAuthorize("permitAll()")
    @GetMapping("createdat/top10/active=true")
    public List<JobResponseDTO> findTop10ByIsActiveTrueOrderByCreatedAtDesc() {
        return jobService.findTop10ByIsActiveTrueOrderByCreatedAtDesc();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("createdat/top20/active=true")
    public List<JobResponseDTO> findTop20ByIsActiveTrueOrderByCreatedAtDesc() {
        return jobService.findTop20ByIsActiveTrueOrderByCreatedAtDesc();
    }


    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("companyprofile/count/{companyProfileId}/active=true")
    public Long countByCompanyProfileIdAndIsActiveTrue(@PathVariable Long companyProfileId) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobService.countByCompanyProfileIdAndIsActiveTrue(companyProfileId);
    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @GetMapping("companyprofile/count/{companyProfileId}/active=false")
    public Long countByCompanyProfileIdAndIsActiveFalse(@PathVariable Long companyProfileId) {
        checkCompanyOwnershipOrAdmin(companyProfileId);
        return jobService.countByCompanyProfileIdAndIsActiveFalse(companyProfileId);
    }

    //Search api
    @PreAuthorize("permitAll()")
    @PostMapping("search")
    public ResponseEntity<List<JobResponseDTO>> search(
            @RequestBody JobSearchRequestDTO dto
    ) {

        return ResponseEntity.ok(

                jobService.search(dto)

        );

    }

    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @PatchMapping("{id}/toggle-status")
    public ResponseEntity<JobResponseDTO> changeJobStatus(@PathVariable Long id) {
        checkJobOwnershipOrAdmin(id);
        return ResponseEntity.ok(jobService.changeJobStatus(id));
    }


    private void checkCompanyOwnership(Long companyProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        CompanyProfileResponseDTO company = companyProfileService.findById(companyProfileId);
        if (!company.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkCompanyOwnershipOrAdmin(Long companyProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        checkCompanyOwnership(companyProfileId);
    }

    private void checkJobOwnership(Long jobId) {
        User currentUser = authenticationService.getCurrentUser();
        JobResponseDTO job = jobService.findById(jobId);
        if (!job.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkJobOwnershipOrAdmin(Long jobId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        checkJobOwnership(jobId);
    }


}
