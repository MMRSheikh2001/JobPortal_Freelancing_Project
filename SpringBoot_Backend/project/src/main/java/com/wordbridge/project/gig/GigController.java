package com.wordbridge.project.gig;


import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/gigs/")
@RequiredArgsConstructor
public class GigController {
    private final GigService gigService;

    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<GigResponseDTO> save(
            @RequestPart("gig") GigRequestDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        checkProfileOwnership(dto.getUserProfileId());

        return new ResponseEntity<>(gigService.save(dto, image)
                , HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<GigResponseDTO>> findAll() {
        List<GigResponseDTO> list = gigService.findAll();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("{id}")
    public ResponseEntity<GigResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(gigService.getById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<GigResponseDTO> update(@PathVariable Long id,
                                                 @RequestPart("gig") GigRequestDTO dto,
                                                 @RequestPart(value = "image", required = false) MultipartFile image) {

        checkGigOwnershipOrAdmin(id);
        GigResponseDTO updated = gigService.update(id, dto, image);
        return ResponseEntity.ok(updated);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("{id}/change")
    public ResponseEntity<GigResponseDTO> changeGigStatus(@PathVariable Long id) {
        checkGigOwnershipOrAdmin(id);
        GigResponseDTO changed = gigService.changeGigStatus(id);
        return ResponseEntity.ok(changed);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkGigOwnershipOrAdmin(id);
        gigService.delete(id);
        return ResponseEntity.ok("Gig Deleted");
    }


    @PreAuthorize("permitAll()")
    @GetMapping("active")
    public ResponseEntity<List<GigResponseDTO>> findByIsActiveTrue() {
        List<GigResponseDTO> list = gigService.findByIsActiveTrue();
        return ResponseEntity.ok(list);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("userprofile/{userProfileId}")
    public ResponseEntity<List<GigResponseDTO>> findByUserProfileId(@PathVariable Long userProfileId) {
        checkProfileOwnershipOrAdmin(userProfileId);
        List<GigResponseDTO> list = gigService.findByUserProfileId(userProfileId);
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("userprofile/{userProfileId}/count")
    public Long countByUserProfileId(@PathVariable Long userProfileId) {
        checkProfileOwnershipOrAdmin(userProfileId);

        return gigService.countByUserProfileId(userProfileId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("userprofile/{userProfileId}/active")
    public ResponseEntity<List<GigResponseDTO>> findByUserProfileIdAndIsActiveTrue(@PathVariable Long userProfileId) {
        List<GigResponseDTO> list = gigService.findByUserProfileIdAndIsActiveTrue(userProfileId);
        return ResponseEntity.ok(list);
    }


    // Search
// ==========================
    @PreAuthorize("permitAll()")
    @PostMapping("search")
    public ResponseEntity<List<GigResponseDTO>> search(
            @RequestBody GigSearchRequestDTO dto
    ) {

        List<GigResponseDTO> list =
                gigService.search(dto);

        return ResponseEntity.ok(list);

    }


    // ==========================
// Latest Gigs
// ==========================
    @PreAuthorize("permitAll()")
    @GetMapping("latest")
    public ResponseEntity<List<GigResponseDTO>> findTop20ByIsActiveTrueOrderByCreatedAtDesc() {

        List<GigResponseDTO> list =
                gigService.findTop20ByIsActiveTrueOrderByCreatedAtDesc();

        return ResponseEntity.ok(list);

    }

    // ==========================
// Top Rated Gigs
// ==========================
    @PreAuthorize("permitAll()")
    @GetMapping("top-rated")
    public ResponseEntity<List<GigResponseDTO>> findTop20ByIsActiveTrueAndTotalReviewsGreaterThanOrderByAverageRatingDesc() {

        List<GigResponseDTO> list =
                gigService.findTop20ByIsActiveTrueAndTotalReviewsGreaterThanOrderByAverageRatingDesc();

        return ResponseEntity.ok(list);

    }

// ==========================
// Popular Gigs
// ==========================

    //Free api
    @PreAuthorize("permitAll()")
    @GetMapping("popular")
    public ResponseEntity<List<GigResponseDTO>> findTop20ByIsActiveTrueOrderByCompletedOrdersDesc() {

        List<GigResponseDTO> list =
                gigService.findTop20ByIsActiveTrueOrderByCompletedOrdersDesc();

        return ResponseEntity.ok(list);

    }

// ==========================
// Related Gigs
// ==========================

    @PreAuthorize("permitAll()")
    @GetMapping("{gigId}/related")
    public ResponseEntity<List<GigResponseDTO>> findRelatedGigs(
            @PathVariable Long gigId
    ) {

        List<GigResponseDTO> list =
                gigService.findRelatedGigs(gigId);

        return ResponseEntity.ok(list);

    }


    // NEW helpers
    private void checkProfileOwnership(Long userProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        UserProfileResponseDTO profile = userProfileService.findById(userProfileId);
        if (!profile.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkProfileOwnershipOrAdmin(Long userProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        checkProfileOwnership(userProfileId);
    }

    private void checkGigOwnershipOrAdmin(Long gigId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        GigResponseDTO gig = gigService.getById(gigId);
        checkProfileOwnership(gig.getUserProfileId());
    }


}
