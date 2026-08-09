package com.wordbridge.project.saved;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/savedjobs/")
@RequiredArgsConstructor
public class SavedJobController {
    public final SavedJobService savedJobService;

    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<SavedJobResponseDTO> saveJob(
            @RequestParam Long userId,
            @RequestParam Long jobId
    ) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                savedJobService.saveJob(userId, jobId)
        );
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    public ResponseEntity<Void> unsaveJob(
            @RequestParam Long userId,
            @RequestParam Long jobId
    ) {
        checkUserIdOwnership(userId);
        savedJobService.unsaveJob(userId, jobId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{userId}")
    public ResponseEntity<List<SavedJobResponseDTO>> getSavedJobs(
            @PathVariable Long userId
    ) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                savedJobService.getSavedJobs(userId)
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("check")
    public ResponseEntity<Boolean> isJobSaved(
            @RequestParam Long userId,
            @RequestParam Long jobId
    ) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                savedJobService.isJobSaved(userId, jobId)
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{userId}/count")
    public Long countByUserId(@PathVariable Long userId) {
        checkUserIdOwnership(userId);
        return savedJobService.countByUserId(userId);
    }


    private void checkUserIdOwnership(Long userId) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }

}
