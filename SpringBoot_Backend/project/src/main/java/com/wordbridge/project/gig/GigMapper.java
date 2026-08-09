package com.wordbridge.project.gig;


import com.wordbridge.project.entity.Category;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.repository.CategoryRepository;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GigMapper {

    private final CategoryRepository categoryRepository;
    private final UserProfileRepository userProfileRepository;

    public GigResponseDTO toDTO(Gig g) {
        GigResponseDTO dto = new GigResponseDTO();

        dto.setId(g.getId());
        dto.setTitle(g.getTitle());
        dto.setShortDescription(g.getShortDescription());
        dto.setDescription(g.getDescription());
        dto.setStartingPrice(g.getStartingPrice());
        dto.setDeliveryDays(g.getDeliveryDays());
        dto.setRevisions(g.getRevisions());
        dto.setGigImage(g.getGigImage());
        dto.setIsActive(g.getIsActive());
        dto.setCreatedAt(g.getCreatedAt());
        dto.setUpdatedAt(g.getUpdatedAt());

        dto.setCategoryId(g.getCategory().getId());
        dto.setCategoryName(g.getCategory().getName());

        dto.setUserProfileId(g.getUserProfile().getId());
        dto.setUserName(g.getUserProfile().getName());


        dto.setAverageRating(g.getAverageRating());
        dto.setTotalReviews(g.getTotalReviews());
        dto.setCompletedOrders(g.getCompletedOrders());


        return dto;

    }

    public Gig toEntity(GigRequestDTO dto) {
        Gig g = new Gig();

        g.setTitle(dto.getTitle());
        g.setShortDescription(dto.getShortDescription());
        g.setDescription(dto.getDescription());
        g.setStartingPrice(dto.getStartingPrice());
        g.setDeliveryDays(dto.getDeliveryDays());
        g.setRevisions(dto.getRevisions());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("No Category found"));

        g.setCategory(category);

        UserProfile userProfile = userProfileRepository.findById(dto.getUserProfileId())
                .orElseThrow(() -> new RuntimeException("No User Profile found"));

        g.setUserProfile(userProfile);


        return g;
    }


}
