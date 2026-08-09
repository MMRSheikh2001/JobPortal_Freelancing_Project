package com.wordbridge.project.saved;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedGigRepository extends JpaRepository<SavedGig,Long> {
    // All saved gigs of a user
    List<SavedGig> findByUserId(Long userId);

    Boolean existsByUserIdAndGigId(Long userId, Long gigId);

    // Check if already saved
    Optional<SavedGig> findByUserIdAndGigId(Long userId, Long gigId);

    // Remove saved gig
    void deleteByUserIdAndGigId(Long userId, Long gigId);

    // (Optional) newest first
    List<SavedGig> findByUserIdOrderByCreatedAtDesc(Long userId);

    Long countByUserId(Long userId);


}
