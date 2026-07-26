package com.wordbridge.project.job;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/jobs/")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;


    @PostMapping
    public ResponseEntity<JobResponseDTO> save(@RequestBody JobRequestDTO dto) {
        JobResponseDTO saved = jobService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAll() {
        List<JobResponseDTO> list = jobService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<JobResponseDTO> getById(@PathVariable Long id) {
        JobResponseDTO dto = jobService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("{id}")
    public ResponseEntity<JobResponseDTO> update(@RequestBody JobRequestDTO dto, @PathVariable Long id) {

        JobResponseDTO updated = jobService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        jobService.delete(id);
        return ResponseEntity.ok("Job Deleted");
    }

    //Search api

    @GetMapping("companyprofile/{id}")
    public List<JobResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return jobService.findByCompanyProfileId(id);
    }


    @GetMapping("user/{userId}")
    public List<JobResponseDTO> findByCompanyProfileUserId(@PathVariable Long userId) {
        return jobService.findByCompanyProfileUserId(userId);
    }

    @GetMapping("companyprofile/count/{companyProfileId}")
    public Long countByCompanyProfileId(@PathVariable Long companyProfileId) {
        return jobService.countByCompanyProfileId(companyProfileId);
    }

    @DeleteMapping("companyprofile/delete/{companyProfileId}")
    public void deleteByCompanyProfileId(@PathVariable Long companyProfileId) {
        jobService.deleteByCompanyProfileId(companyProfileId);
    }

    @GetMapping("active=true")
    public List<JobResponseDTO> findByIsActiveTrue() {
        return jobService.findByIsActiveTrue();
    }


    @GetMapping("companyprofile/{companyProfileId}/active=true")
    public List<JobResponseDTO> findByCompanyProfileIdAndIsActiveTrue(@PathVariable Long companyProfileId) {
        return jobService.findByCompanyProfileIdAndIsActiveTrue(companyProfileId);
    }

    //free api
    @GetMapping("createdat/top10/active=true")
    public List<JobResponseDTO> findTop10ByIsActiveTrueOrderByCreatedAtDesc() {
        return jobService.findTop10ByIsActiveTrueOrderByCreatedAtDesc();
    }

    @GetMapping("createdat/top20/active=true")
    public List<JobResponseDTO> findTop20ByIsActiveTrueOrderByCreatedAtDesc() {
        return jobService.findTop20ByIsActiveTrueOrderByCreatedAtDesc();
    }


    @GetMapping("companyprofile/count/{companyProfileId}/active=true")
    public Long countByCompanyProfileIdAndIsActiveTrue(@PathVariable Long companyProfileId) {
        return jobService.countByCompanyProfileIdAndIsActiveTrue(companyProfileId);
    }

    @GetMapping("companyprofile/count/{companyProfileId}/active=false")
    public Long countByCompanyProfileIdAndIsActiveFalse(@PathVariable Long companyProfileId) {
        return jobService.countByCompanyProfileIdAndIsActiveFalse(companyProfileId);
    }

    //Search api
    @PostMapping("search")
    public ResponseEntity<List<JobResponseDTO>> search(
            @RequestBody JobSearchRequestDTO dto
    ) {

        return ResponseEntity.ok(

                jobService.search(dto)

        );

    }

    @PatchMapping("{id}/toggle-status")
    public ResponseEntity<JobResponseDTO> changeJobStatus(@PathVariable Long id){
        return ResponseEntity.ok(jobService.changeJobStatus(id));
    }

}
