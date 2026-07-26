package com.wordbridge.project.repository;


import com.wordbridge.project.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training,Long> {
    List<Training> findByUserProfileId(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);

    void deleteByUserProfileId(Long userProfileId);


}
