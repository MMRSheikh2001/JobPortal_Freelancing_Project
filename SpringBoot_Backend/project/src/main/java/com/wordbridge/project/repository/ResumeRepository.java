package com.wordbridge.project.repository;

import com.wordbridge.project.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findByUserProfileId(Long userProfileId);

    boolean existsByUserProfileId(Long userProfileId);

    void deleteByUserProfileId(Long userProfileId);

}
