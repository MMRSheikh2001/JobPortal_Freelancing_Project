package com.wordbridge.project.saved;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob,Long> {

    // All saved jobs of a user
    List<SavedJob> findByUserId(Long userId);



    // Check if already saved
    Optional<SavedJob> findByUserIdAndJobId(Long userId, Long jobId);

    // Remove saved job
    void deleteByUserIdAndJobId(Long userId, Long jobId);

    // (Optional) newest first
    List<SavedJob> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    Long countByUserId(Long userId);
}
