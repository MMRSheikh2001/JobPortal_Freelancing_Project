package com.wordbridge.project.review;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {

    ReviewResponseDTO create(ReviewRequestDTO dto);
    List<ReviewResponseDTO> findAll();
    ReviewResponseDTO getById(Long id);
    ReviewResponseDTO update(Long id,ReviewRequestDTO dto);
    void delete(Long id);


    ReviewResponseDTO findByGigOrderId(Long gigOrderId);

    // Reviews of a seller
    List<ReviewResponseDTO> findByGigOrderGigUserProfileId(Long sellerUserProfileId);

    // Reviews written by a buyer
    List<ReviewResponseDTO> findByGigOrderBuyerId(Long buyerId);

    // Reviews for a gig
    List<ReviewResponseDTO> findByGigOrderGigId(Long gigId);

    //Find specific rating reviews
    List<ReviewResponseDTO> findByRating(Integer rating);

    long countByGigOrderGigUserProfileId(Long sellerUserProfileId);
    long countByGigOrderGigId(Long gigId);

    Boolean existsByGigOrderId(Long gigOrderId);

}
