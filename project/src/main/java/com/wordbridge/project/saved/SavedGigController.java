package com.wordbridge.project.saved;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/savedgigs/")
@RequiredArgsConstructor
public class SavedGigController {
    private final SavedGigService savedGigService;

    @PostMapping
    public ResponseEntity<SavedGigResponseDTO> saveGig(
            @RequestParam Long userId,
            @RequestParam Long gigId
    ) {
        return ResponseEntity.ok(
                savedGigService.saveGig(userId, gigId)
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> unsaveGig(
            @RequestParam Long userId,
            @RequestParam Long gigId
    ) {
        savedGigService.unsaveGig(userId, gigId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<SavedGigResponseDTO>> getSavedGigs(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                savedGigService.getSavedGigs(userId)
        );
    }

    @GetMapping("check")
    public ResponseEntity<Boolean> isGigSaved(
            @RequestParam Long userId,
            @RequestParam Long gigId
    ) {
        return ResponseEntity.ok(
                savedGigService.isGigSaved(userId, gigId)
        );
    }

    @GetMapping("{userId}/count")
    public Long countByUserId(@PathVariable Long userId) {
        return savedGigService.countByUserId(userId);
    }


}
