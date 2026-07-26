package com.wordbridge.project.gig;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface GigService {

    GigResponseDTO save(GigRequestDTO dto, MultipartFile image);

    List<GigResponseDTO> findAll();

    GigResponseDTO getById(Long id);

    GigResponseDTO update(Long id, GigRequestDTO dto, MultipartFile image);

    GigResponseDTO changeGigStatus(Long id);

    void delete(Long id);


    List<GigResponseDTO> findByIsActiveTrue();

    List<GigResponseDTO> findByUserProfileId(Long userProfileId);


    List<GigResponseDTO> findByUserProfileIdAndIsActiveTrue(Long userProfileId);


    // ==========================
// Search
// ==========================

    List<GigResponseDTO> search(
            GigSearchRequestDTO dto
    );

    //count
    Long countByUserProfileId(Long userProfileId);

    Long countByUserProfileIdAndIsActiveTrue(Long userProfileId);

    Long countByUserProfileIdAndIsActiveFalse(Long userProfileId);

    // ==========================
    // Latest Gigs
    // ==========================

    List<GigResponseDTO> findTop20ByIsActiveTrueOrderByCreatedAtDesc();

    // ==========================
    // Top Rated Gigs
    // ==========================

    List<GigResponseDTO> findTop20ByIsActiveTrueAndTotalReviewsGreaterThanOrderByAverageRatingDesc();

    // ==========================
    // Popular Gigs
    // ==========================

    List<GigResponseDTO> findTop20ByIsActiveTrueOrderByCompletedOrdersDesc();

    // ==========================
    // Related Gigs
    // ==========================

    List<GigResponseDTO> findRelatedGigs(Long gigId);

    List<GigResponseDTO> getPopularGigs();

    Long countFreelancers();

    Long countClients();

    Long countActiveFreelancers();

    Long countTotalGig();

    Long countActiveGigs();

    Long countInactiveGigs();

    Double getFreelancerAverageRating(Long userProfileId);

    Long getTotalReviews(Long userProfileId);

    List<GigResponseDTO> getPopularGigsByFreelancer(Long userProfileId);

}
