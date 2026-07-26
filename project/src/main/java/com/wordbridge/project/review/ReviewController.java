package com.wordbridge.project.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews/")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(@RequestBody ReviewRequestDTO dto) {
        ReviewResponseDTO created = reviewService.create(dto);

        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAll() {
        List<ReviewResponseDTO> list = reviewService.findAll();

        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<ReviewResponseDTO> getById(@PathVariable Long id) {
        ReviewResponseDTO dto = reviewService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("{id}")
    public ResponseEntity<ReviewResponseDTO> update(@PathVariable Long id,
                                                    @RequestBody ReviewRequestDTO dto) {
        ReviewResponseDTO updated = reviewService.update(id, dto);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok("Review deleted successfully");
    }


    @GetMapping("gig-order/{gigOrderId}")
    public ReviewResponseDTO findByGigOrderId(@PathVariable Long gigOrderId) {

        return reviewService.findByGigOrderId(gigOrderId);
    }

    @GetMapping("seller/{sellerUserProfileId}")
    public List<ReviewResponseDTO> findByGigOrderGigUserProfileId(@PathVariable Long sellerUserProfileId) {
        return reviewService.findByGigOrderGigUserProfileId(sellerUserProfileId);
    }

    @GetMapping("buyer/{buyerId}")
    public List<ReviewResponseDTO> findByGigOrderBuyerId(@PathVariable Long buyerId) {
        return reviewService.findByGigOrderBuyerId(buyerId);
    }

    @GetMapping("gig/{gigId}")
    public List<ReviewResponseDTO> findByGigOrderGigId(@PathVariable Long gigId) {
        return reviewService.findByGigOrderGigId(gigId);
    }

    @GetMapping("rating/{rating}")
    public List<ReviewResponseDTO> findByRating(@PathVariable Integer rating) {
        return reviewService.findByRating(rating);
    }

    @GetMapping("seller/{sellerUserProfileId}/count")
    public long countByGigOrderGigUserProfileId(@PathVariable Long sellerUserProfileId) {
        return reviewService.countByGigOrderGigUserProfileId(sellerUserProfileId);
    }

    @GetMapping("gig/{gigId}/count")
    public long countByGigOrderGigId(@PathVariable Long gigId) {
        return reviewService.countByGigOrderGigId(gigId);
    }


    @GetMapping("gig-order/{gigOrderId}/exists")
    public Boolean existsByGigOrderId(@PathVariable Long gigOrderId) {
        return reviewService.existsByGigOrderId(gigOrderId);
    }

}
