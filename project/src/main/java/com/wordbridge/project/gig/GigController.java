package com.wordbridge.project.gig;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/gigs/")
@RequiredArgsConstructor
public class GigController {
    private final GigService gigService;

    @PostMapping
    public ResponseEntity<GigResponseDTO> save(
            @RequestPart("gig") GigRequestDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return new ResponseEntity<>(gigService.save(dto, image)
                , HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GigResponseDTO>> findAll() {
        List<GigResponseDTO> list = gigService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<GigResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(gigService.getById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<GigResponseDTO> update(@PathVariable Long id,
                                                 @RequestPart("gig") GigRequestDTO dto,
                                                 @RequestPart(value = "image", required = false) MultipartFile image) {
        GigResponseDTO updated = gigService.update(id, dto, image);
        return ResponseEntity.ok(updated);

    }

    @PatchMapping("{id}/change")
    public ResponseEntity<GigResponseDTO> changeGigStatus(@PathVariable Long id) {
        GigResponseDTO changed = gigService.changeGigStatus(id);
        return ResponseEntity.ok(changed);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        gigService.delete(id);
        return ResponseEntity.ok("Gig Deleted");
    }


    @GetMapping("active")
    public ResponseEntity<List<GigResponseDTO>> findByIsActiveTrue() {
        List<GigResponseDTO> list = gigService.findByIsActiveTrue();
        return ResponseEntity.ok(list);

    }

    @GetMapping("userprofile/{userProfileId}")
    public ResponseEntity<List<GigResponseDTO>> findByUserProfileId(@PathVariable Long userProfileId) {
        List<GigResponseDTO> list = gigService.findByUserProfileId(userProfileId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("userprofile/{userProfileId}/count")
    public Long countByUserProfileId(@PathVariable Long userProfileId) {

        return gigService.countByUserProfileId(userProfileId);
    }

    @GetMapping("userprofile/{userProfileId}/active")
    public ResponseEntity<List<GigResponseDTO>> findByUserProfileIdAndIsActiveTrue(@PathVariable Long userProfileId) {
        List<GigResponseDTO> list = gigService.findByUserProfileIdAndIsActiveTrue(userProfileId);
        return ResponseEntity.ok(list);
    }


// Search
// ==========================

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

    @GetMapping("latest")
    public ResponseEntity<List<GigResponseDTO>> findTop20ByIsActiveTrueOrderByCreatedAtDesc() {

        List<GigResponseDTO> list =
                gigService.findTop20ByIsActiveTrueOrderByCreatedAtDesc();

        return ResponseEntity.ok(list);

    }

// ==========================
// Top Rated Gigs
// ==========================

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
    @GetMapping("popular")
    public ResponseEntity<List<GigResponseDTO>> findTop20ByIsActiveTrueOrderByCompletedOrdersDesc() {

        List<GigResponseDTO> list =
                gigService.findTop20ByIsActiveTrueOrderByCompletedOrdersDesc();

        return ResponseEntity.ok(list);

    }

// ==========================
// Related Gigs
// ==========================

    @GetMapping("{gigId}/related")
    public ResponseEntity<List<GigResponseDTO>> findRelatedGigs(
            @PathVariable Long gigId
    ) {

        List<GigResponseDTO> list =
                gigService.findRelatedGigs(gigId);

        return ResponseEntity.ok(list);

    }


}
