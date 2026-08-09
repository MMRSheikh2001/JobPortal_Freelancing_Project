package com.wordbridge.project.gig;


import com.wordbridge.project.util.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GigServiceImpl implements GigService {
    private final GigRepository gigRepository;
    private final GigMapper gigMapper;
    private final ImageStorageService imageStorageService;


    @Override
    @Transactional
    public GigResponseDTO save(GigRequestDTO dto, MultipartFile image) {
        Gig gig = gigMapper.toEntity(dto);
        if (image != null && !image.isEmpty()) {
            String fileName = imageStorageService.uploadImage(image, gig.getUserProfile().getUser().getEmail(), "gigs");
            gig.setGigImage(fileName);

        }
        gig.setIsActive(true);
        gig.setAverageRating(0.0);
        gig.setTotalReviews(0);
        gig.setCompletedOrders(0);

        return gigMapper.toDTO(gigRepository.save(gig));
    }

    @Override
    public List<GigResponseDTO> findAll() {
        return gigRepository.findAll().stream().map(gigMapper::toDTO).toList();
    }

    @Override
    public GigResponseDTO getById(Long id) {
        Gig gig = gigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Gig found"));
        return gigMapper.toDTO(gig);
    }

    @Override
    @Transactional
    public GigResponseDTO update(Long id, GigRequestDTO dto, MultipartFile image) {

        Gig exist = gigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Gig found"));

        Gig gig = gigMapper.toEntity(dto);
        gig.setId(id);
        gig.setCreatedAt(exist.getCreatedAt());
        gig.setUpdatedAt(LocalDateTime.now());
        gig.setIsActive(exist.getIsActive());

        gig.setAverageRating(exist.getAverageRating());
        gig.setTotalReviews(exist.getTotalReviews());
        gig.setCompletedOrders(exist.getCompletedOrders());


        if (image != null && !image.isEmpty()) {
            String fileName = imageStorageService.uploadImage(image, gig.getUserProfile().getUser().getEmail(), "gigs");
            if (exist.getGigImage() != null) {
                imageStorageService.deleteImage("gigs", exist.getGigImage());
            }
            gig.setGigImage(fileName);


        } else {
            gig.setGigImage(exist.getGigImage());
        }
        Gig updated = gigRepository.save(gig);

        return gigMapper.toDTO(updated);
    }

    @Override
    public GigResponseDTO changeGigStatus(Long id) {
        Gig exist = gigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Gig found"));

        if (exist.getIsActive()) {
            exist.setIsActive(false);
        } else {
            exist.setIsActive(true);
        }


        return gigMapper.toDTO(gigRepository.save(exist));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Gig exist = gigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Gig found"));
        if (exist.getGigImage() != null) {
            imageStorageService.deleteImage("gigs", exist.getGigImage());
        }
        gigRepository.delete(exist);

    }

    @Override
    public List<GigResponseDTO> findByIsActiveTrue() {
        return gigRepository.findByIsActiveTrue().stream().map(gigMapper::toDTO).toList();
    }

    @Override
    public List<GigResponseDTO> findByUserProfileId(Long userProfileId) {
        return gigRepository.findByUserProfileId(userProfileId).stream().map(gigMapper::toDTO).toList();
    }


    @Override
    public List<GigResponseDTO> findByUserProfileIdAndIsActiveTrue(Long userProfileId) {
        return gigRepository.findByUserProfileIdAndIsActiveTrue(userProfileId).stream().map(gigMapper::toDTO).toList();
    }


// Search
// ==========================

    @Override
    public List<GigResponseDTO> search(
            GigSearchRequestDTO dto
    ) {

        Specification<Gig> specification =
                GigSpecification.filter(

                        dto.getKeyword(),
                        dto.getCategoryId(),
                        dto.getMinPrice(),
                        dto.getMaxPrice(),
                        dto.getMaxDeliveryDays(),
                        dto.getActive(),
                        dto.getMinimumRating(),
                        dto.getMinimumOrders()

                );

        return gigRepository
                .findAll(specification)
                .stream()
                .map(gigMapper::toDTO)
                .toList();

    }

    @Override
    public Long countByUserProfileId(Long userProfileId) {
        return gigRepository.countByUserProfileId(userProfileId);
    }

    @Override
    public Long countByUserProfileIdAndIsActiveTrue(Long userProfileId) {
        return gigRepository.countByUserProfileIdAndIsActiveTrue(userProfileId);
    }

    @Override
    public Long countByUserProfileIdAndIsActiveFalse(Long userProfileId) {
        return gigRepository.countByUserProfileIdAndIsActiveFalse(userProfileId);
    }

    @Override
    public List<GigResponseDTO> findTop20ByIsActiveTrueOrderByCreatedAtDesc() {
        return gigRepository.findTop20ByIsActiveTrueOrderByCreatedAtDesc().stream().map(gigMapper::toDTO).toList();
    }

    @Override
    public List<GigResponseDTO> findTop20ByIsActiveTrueAndTotalReviewsGreaterThanOrderByAverageRatingDesc() {
        return gigRepository.findTop20ByIsActiveTrueAndTotalReviewsGreaterThanOrderByAverageRatingDesc(0).stream().map(gigMapper::toDTO).toList();
    }

    @Override
    public List<GigResponseDTO> findTop20ByIsActiveTrueOrderByCompletedOrdersDesc() {
        return gigRepository.findTop20ByIsActiveTrueOrderByCompletedOrdersDesc().stream().map(gigMapper::toDTO).toList();
    }

    @Override
    public List<GigResponseDTO> findRelatedGigs(Long gigId) {
        Long categoryId = gigRepository.findById(gigId).orElseThrow(() -> new RuntimeException("No gig found")).getCategory().getId();
        return gigRepository.findTop10ByCategoryIdAndIsActiveTrueAndIdNot(categoryId, gigId).stream().map(gigMapper::toDTO).toList();
    }

    @Override
    public List<GigResponseDTO> getPopularGigs() {
        return gigRepository.findTop10ByIsActiveTrueOrderByCompletedOrdersDescAverageRatingDesc().stream()
                .map(gigMapper::toDTO).toList();
    }

    @Override
    public Long countFreelancers() {
        return gigRepository.countFreelancers();
    }

    @Override
    public Long countClients() {
        return gigRepository.countClients();
    }

    @Override
    public Long countActiveFreelancers() {
        return gigRepository.countActiveFreelancers();
    }

    @Override
    public Long countTotalGig() {
        return gigRepository.count();
    }

    @Override
    public Long countActiveGigs() {
        return gigRepository.countByIsActiveTrue();
    }

    @Override
    public Long countInactiveGigs() {
        return gigRepository.countByIsActiveFalse();
    }

    @Override
    public Double getFreelancerAverageRating(Long userProfileId) {

        List<Gig> gigs = gigRepository.findByUserProfileId(userProfileId);

        double totalRating = 0;
        long totalReviews = 0;

        for (Gig gig : gigs) {

            if (gig.getAverageRating() != null && gig.getTotalReviews() != null) {

                totalRating += gig.getAverageRating() * gig.getTotalReviews();
                totalReviews += gig.getTotalReviews();
            }
        }

        if (totalReviews == 0) {
            return 0.0;
        }

        return totalRating / totalReviews;
    }

    @Override
    public Long getTotalReviews(Long userProfileId) {
        return gigRepository.getTotalReviews(userProfileId);
    }


    @Override
    public List<GigResponseDTO> getPopularGigsByFreelancer(
            Long userProfileId
    ) {

        Pageable pageable = PageRequest.of(0, 5);

        return gigRepository
                .findByUserProfileIdOrderByCompletedOrdersDescAverageRatingDesc(
                        userProfileId,
                        pageable
                )
                .stream()
                .map(gigMapper::toDTO)
                .toList();
    }
}
