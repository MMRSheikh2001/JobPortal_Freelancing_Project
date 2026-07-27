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
@RequestMapping("/api/savedgigs/")
@RequiredArgsConstructor
public class SavedGigController {
    private final SavedGigService savedGigService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @PostMapping
    public ResponseEntity<SavedGigResponseDTO> saveGig(
            @RequestParam Long userId,
            @RequestParam Long gigId
    ) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                savedGigService.saveGig(userId, gigId)
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @DeleteMapping
    public ResponseEntity<Void> unsaveGig(
            @RequestParam Long userId,
            @RequestParam Long gigId
    ) {
        checkUserIdOwnership(userId);
        savedGigService.unsaveGig(userId, gigId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @GetMapping("{userId}")
    public ResponseEntity<List<SavedGigResponseDTO>> getSavedGigs(
            @PathVariable Long userId
    ) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                savedGigService.getSavedGigs(userId)
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @GetMapping("check")
    public ResponseEntity<Boolean> isGigSaved(
            @RequestParam Long userId,
            @RequestParam Long gigId
    ) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                savedGigService.isGigSaved(userId, gigId)
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @GetMapping("{userId}/count")
    public Long countByUserId(@PathVariable Long userId) {
        checkUserIdOwnership(userId);
        return savedGigService.countByUserId(userId);
    }



    private void checkUserIdOwnership(Long userId) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }


}
