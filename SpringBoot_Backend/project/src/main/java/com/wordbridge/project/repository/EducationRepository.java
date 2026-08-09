package com.wordbridge.project.repository;

import com.wordbridge.project.entity.Education;
import com.wordbridge.project.enums.EducationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education,Long> {
    List<Education> findByUserProfileId(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);

    void deleteByUserProfileId(Long userProfileId);


    boolean existsByUserProfileIdAndEducationLevelAndInstitutionIgnoreCaseAndFieldOfStudyIgnoreCase(
            Long userProfileId,
            EducationLevel educationLevel,
            String institution,
            String fieldOfStudy
    );


}
