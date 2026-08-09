package com.wordbridge.project.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // One review per order
    boolean existsByGigOrderId(Long gigOrderId);

    Optional<Review> findByGigOrderId(Long gigOrderId);

    // Reviews of a seller
    List<Review> findByGigOrderGigUserProfileId(Long sellerUserProfileId);

    // Reviews written by a buyer
    List<Review> findByGigOrderBuyerId(Long buyerId);

    // Reviews for a gig
    List<Review> findByGigOrderGigId(Long gigId);

    //Find specific rating reviews
    List<Review> findByRating(Integer rating);

    long countByGigOrderGigUserProfileId(Long sellerUserProfileId);
    long countByGigOrderGigId(Long gigId);

}
