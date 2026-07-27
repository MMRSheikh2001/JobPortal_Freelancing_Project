package com.wordbridge.project.review;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.gigorder.GigOrderResponseDTO;
import com.wordbridge.project.gigorder.GigOrderService;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews/")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    private final GigOrderService gigOrderService;
    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(@RequestBody ReviewRequestDTO dto) {

        checkOrderBuyerOwnership(dto.getGigOrderId());
        ReviewResponseDTO created = reviewService.create(dto);

        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAll() {
        List<ReviewResponseDTO> list = reviewService.findAll();

        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<ReviewResponseDTO> getById(@PathVariable Long id) {
        checkReviewAccess(id);
        ReviewResponseDTO dto = reviewService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @PutMapping("{id}")
    public ResponseEntity<ReviewResponseDTO> update(@PathVariable Long id,
                                                    @RequestBody ReviewRequestDTO dto) {
        checkReviewerOwnership(id);
        ReviewResponseDTO updated = reviewService.update(id, dto);
        return ResponseEntity.ok(updated);

    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkReviewerOrAdminOwnership(id);
        reviewService.delete(id);
        return ResponseEntity.ok("Review deleted successfully");
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("gig-order/{gigOrderId}")
    public ReviewResponseDTO findByGigOrderId(@PathVariable Long gigOrderId) {

        checkOrderPartyOrAdmin(gigOrderId);
        return reviewService.findByGigOrderId(gigOrderId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("seller/{sellerUserProfileId}")
    public List<ReviewResponseDTO> findByGigOrderGigUserProfileId(@PathVariable Long sellerUserProfileId) {
        return reviewService.findByGigOrderGigUserProfileId(sellerUserProfileId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("buyer/{buyerId}")
    public List<ReviewResponseDTO> findByGigOrderBuyerId(@PathVariable Long buyerId) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(buyerId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }

        return reviewService.findByGigOrderBuyerId(buyerId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("gig/{gigId}")
    public List<ReviewResponseDTO> findByGigOrderGigId(@PathVariable Long gigId) {
        return reviewService.findByGigOrderGigId(gigId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("rating/{rating}")
    public List<ReviewResponseDTO> findByRating(@PathVariable Integer rating) {
        return reviewService.findByRating(rating);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("seller/{sellerUserProfileId}/count")
    public long countByGigOrderGigUserProfileId(@PathVariable Long sellerUserProfileId) {
        return reviewService.countByGigOrderGigUserProfileId(sellerUserProfileId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("gig/{gigId}/count")
    public long countByGigOrderGigId(@PathVariable Long gigId) {
        return reviewService.countByGigOrderGigId(gigId);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("gig-order/{gigOrderId}/exists")
    public Boolean existsByGigOrderId(@PathVariable Long gigOrderId) {
        checkOrderPartyOrAdmin(gigOrderId);
        return reviewService.existsByGigOrderId(gigOrderId);
    }


    //  private methods

    private void checkOrderBuyerOwnership(Long gigOrderId) {
        User currentUser = authenticationService.getCurrentUser();
        GigOrderResponseDTO order = gigOrderService.getById(gigOrderId);
        if (!order.getBuyerId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkOrderPartyOrAdmin(Long gigOrderId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        GigOrderResponseDTO order = gigOrderService.getById(gigOrderId);
        boolean isBuyer = order.getBuyerId().equals(currentUser.getId());
        boolean isSeller = userProfileService.findById(order.getSellerId()).getUserId().equals(currentUser.getId());
        if (!isBuyer && !isSeller) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkReviewerOwnership(Long reviewId) {
        User currentUser = authenticationService.getCurrentUser();
        ReviewResponseDTO review = reviewService.getById(reviewId);
        if (!review.getReviewerId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkReviewerOrAdminOwnership(Long reviewId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        checkReviewerOwnership(reviewId);
    }

    private void checkReviewAccess(Long reviewId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        ReviewResponseDTO review = reviewService.getById(reviewId);
        boolean isReviewer = review.getReviewerId().equals(currentUser.getId());
        boolean isSeller = userProfileService.findById(review.getSellerUserProfileId()).getUserId().equals(currentUser.getId());
        if (!isReviewer && !isSeller) {
            throw new AccessDeniedException("Not allowed");
        }
    }

}
