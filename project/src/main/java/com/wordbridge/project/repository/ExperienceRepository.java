package com.wordbridge.project.repository;

import com.wordbridge.project.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    List<Experience> findByUserProfileId(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);

    void deleteByUserProfileId(Long userProfileId);


}
