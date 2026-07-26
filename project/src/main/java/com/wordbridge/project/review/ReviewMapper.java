package com.wordbridge.project.review;

import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.gigorder.GigOrder;
import com.wordbridge.project.gigorder.GigOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final GigOrderRepository gigOrderRepository;

    public ReviewResponseDTO toDTO(Review r) {
        ReviewResponseDTO dto = new ReviewResponseDTO();

        dto.setId(r.getId());
        dto.setRating(r.getRating());
        dto.setComment(r.getComment());
        dto.setCreatedAt(r.getCreatedAt());

        dto.setGigOrderId(r.getGigOrder().getId());
        dto.setReviewerId(r.getGigOrder().getBuyer().getId());

        if (r.getGigOrder().getBuyer().getRole() == UserRole.USER) {
            dto.setReviewerName(r.getGigOrder().getBuyer().getUserProfile().getName());
        } else {
            dto.setReviewerName(r.getGigOrder().getBuyer().getCompanyProfile().getName());
        }

        dto.setSellerUserProfileId(r.getGigOrder().getGig().getUserProfile().getId());
        dto.setSellerName(r.getGigOrder().getGig().getUserProfile().getName());

        dto.setGigId(r.getGigOrder().getGig().getId());
        dto.setGigTitle(r.getGigOrder().getGig().getTitle());


        return dto;
    }


    public Review toEntity(ReviewRequestDTO dto) {
        Review r = new Review();

        r.setRating(dto.getRating());
        r.setComment(dto.getComment());

        GigOrder gigOrder = gigOrderRepository.findById(dto.getGigOrderId())
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));
        r.setGigOrder(gigOrder);


        return r;
    }

}
