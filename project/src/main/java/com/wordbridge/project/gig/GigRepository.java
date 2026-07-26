package com.wordbridge.project.gig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GigRepository extends JpaRepository<Gig, Long>, JpaSpecificationExecutor<Gig> {

    List<Gig> findByIsActiveTrue();

    List<Gig> findByUserProfileId(Long userProfileId);


    List<Gig> findByUserProfileIdAndIsActiveTrue(Long userProfileId);

    Long countByUserProfileIdAndIsActiveTrue(Long userProfileId);

    Long countByUserProfileIdAndIsActiveFalse(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);


    // ==========================
    // Latest Gigs
    // ==========================

    List<Gig> findTop20ByIsActiveTrueOrderByCreatedAtDesc();

    // ==========================
    // Top Rated Gigs
    // ==========================

    List<Gig> findTop20ByIsActiveTrueAndTotalReviewsGreaterThanOrderByAverageRatingDesc(
            Integer totalReviews
    );

    // ==========================
    // Popular Gigs
    // ==========================

    List<Gig> findTop20ByIsActiveTrueOrderByCompletedOrdersDesc();

    // ==========================
    // Related Gigs
    // ==========================

    List<Gig> findTop10ByCategoryIdAndIsActiveTrueAndIdNot(
            Long categoryId,
            Long gigId
    );


    List<Gig> findTop10ByIsActiveTrueOrderByCompletedOrdersDescAverageRatingDesc();

    @Query("""
                SELECT COUNT(DISTINCT g.userProfile.id)
                FROM Gig g
            """)
    Long countFreelancers();

    @Query("""
                SELECT COUNT(DISTINCT go.buyer.id)
                FROM GigOrder go
            """)
    Long countClients();

    @Query("""
                SELECT COUNT(DISTINCT g.userProfile.id)
                FROM Gig g
                WHERE g.isActive = true
            """)
    Long countActiveFreelancers();


    Long countByIsActiveTrue();


    Long countByIsActiveFalse();

    @Query("""
            SELECT COALESCE(SUM(g.totalReviews),0)
            FROM Gig g
            WHERE g.userProfile.id = :userProfileId
            """)
    Long getTotalReviews(
            @Param("userProfileId") Long userProfileId
    );

    Page<Gig> findByUserProfileIdOrderByCompletedOrdersDescAverageRatingDesc(
            Long userProfileId,
            Pageable pageable
    );

}
