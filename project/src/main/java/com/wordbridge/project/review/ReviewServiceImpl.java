package com.wordbridge.project.review;

import com.wordbridge.project.enums.GigOrderStatus;
import com.wordbridge.project.gig.Gig;
import com.wordbridge.project.gig.GigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final GigRepository gigRepository;


    @Override
    @Transactional
    public ReviewResponseDTO create(ReviewRequestDTO dto) {
        if (reviewRepository.existsByGigOrderId(dto.getGigOrderId())) {
            throw new RuntimeException("Review already exists for this order.");
        }
        Review review = reviewMapper.toEntity(dto);

        if (review.getGigOrder().getStatus() != GigOrderStatus.BUYER_ACCEPTED &&
                review.getGigOrder().getStatus() != GigOrderStatus.PAYMENT_RELEASED) {

            throw new RuntimeException("This order cannot be reviewed.");
        }
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5.");
        }

        Review saved = reviewRepository.save(review);

        Gig gig = saved.getGigOrder().getGig();
        Integer totalReviews = gig.getTotalReviews() + 1;
        gig.setTotalReviews(totalReviews);

        Double averageRating = (gig.getAverageRating() * (totalReviews - 1) + saved.getRating()) / totalReviews;
        gig.setAverageRating(averageRating);
        gigRepository.save(gig);

        return reviewMapper.toDTO(saved);
    }

    @Override
    public List<ReviewResponseDTO> findAll() {
        return reviewRepository.findAll().stream().map(reviewMapper::toDTO).toList();
    }

    @Override
    public ReviewResponseDTO getById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Review found"));
        return reviewMapper.toDTO(review);
    }

    @Override
    @Transactional
    public ReviewResponseDTO update(Long id, ReviewRequestDTO dto) {
        Review exist = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Review found"));
        Review review = reviewMapper.toEntity(dto);
        review.setId(exist.getId());
        review.setCreatedAt(exist.getCreatedAt());

        Review updated = reviewRepository.save(review);

        Gig gig = updated.getGigOrder().getGig();

        Double averageRating = (gig.getAverageRating() * gig.getTotalReviews() - exist.getRating() + updated.getRating()) / gig.getTotalReviews();
        gig.setAverageRating(averageRating);
        gigRepository.save(gig);

        return reviewMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Review exist = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Review found"));


        Gig gig = exist.getGigOrder().getGig();
        Integer totalReviews = gig.getTotalReviews() - 1;
        gig.setTotalReviews(totalReviews);

        Double averageRating = (gig.getAverageRating() * (totalReviews + 1) - exist.getRating()) / totalReviews;
        gig.setAverageRating(averageRating);
        gigRepository.save(gig);


        reviewRepository.delete(exist);


    }

    @Override
    public ReviewResponseDTO findByGigOrderId(Long gigOrderId) {
        Review review = reviewRepository.findByGigOrderId(gigOrderId)
                .orElseThrow(() -> new RuntimeException("No Review found"));
        return reviewMapper.toDTO(review);
    }

    @Override
    public List<ReviewResponseDTO> findByGigOrderGigUserProfileId(Long sellerUserProfileId) {
        return reviewRepository.findByGigOrderGigUserProfileId(sellerUserProfileId).stream().map(reviewMapper::toDTO).toList();
    }

    @Override
    public List<ReviewResponseDTO> findByGigOrderBuyerId(Long buyerId) {
        return reviewRepository.findByGigOrderBuyerId(buyerId).stream().map(reviewMapper::toDTO).toList();
    }

    @Override
    public List<ReviewResponseDTO> findByGigOrderGigId(Long gigId) {
        return reviewRepository.findByGigOrderGigId(gigId).stream().map(reviewMapper::toDTO).toList();
    }

    @Override
    public List<ReviewResponseDTO> findByRating(Integer rating) {
        return reviewRepository.findByRating(rating).stream().map(reviewMapper::toDTO).toList();
    }

    @Override
    public long countByGigOrderGigUserProfileId(Long sellerUserProfileId) {
        return reviewRepository.countByGigOrderGigUserProfileId(sellerUserProfileId);
    }

    @Override
    public long countByGigOrderGigId(Long gigId) {
        return reviewRepository.countByGigOrderGigId(gigId);
    }

    @Override
    public Boolean existsByGigOrderId(Long gigOrderId) {
        return reviewRepository.existsByGigOrderId(gigOrderId);
    }

}
