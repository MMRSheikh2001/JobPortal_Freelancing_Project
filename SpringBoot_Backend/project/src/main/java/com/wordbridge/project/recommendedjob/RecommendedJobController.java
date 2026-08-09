package com.wordbridge.project.recommendedjob;

import com.wordbridge.project.job.JobResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommended-job/")
@RequiredArgsConstructor
public class RecommendedJobController {

    private final RecommendedJobService recommendedJobService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{userProfileId}")
    public ResponseEntity<List<JobResponseDTO>> getRecommendedJobs(
            @PathVariable Long userProfileId) {

        return ResponseEntity.ok(
                recommendedJobService.getRecommendedJobs(userProfileId)
        );
    }

}
