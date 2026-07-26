package com.wordbridge.project.saved;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/savedjobs/")
@RequiredArgsConstructor
public class SavedJobController {
    public final SavedJobService savedJobService;

    @PostMapping
    public ResponseEntity<SavedJobResponseDTO> saveJob(
            @RequestParam Long userId,
            @RequestParam Long jobId
    ) {
        return ResponseEntity.ok(
                savedJobService.saveJob(userId, jobId)
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> unsaveJob(
            @RequestParam Long userId,
            @RequestParam Long jobId
    ) {
        savedJobService.unsaveJob(userId, jobId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<SavedJobResponseDTO>> getSavedJobs(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                savedJobService.getSavedJobs(userId)
        );
    }

    @GetMapping("check")
    public ResponseEntity<Boolean> isJobSaved(
            @RequestParam Long userId,
            @RequestParam Long jobId
    ) {
        return ResponseEntity.ok(
                savedJobService.isJobSaved(userId, jobId)
        );
    }

    @GetMapping("{userId}/count")
    public Long countByUserId(@PathVariable Long userId) {
        return savedJobService.countByUserId(userId);
    }

}
