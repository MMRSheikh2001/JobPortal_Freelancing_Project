package com.wordbridge.project.repository;

import com.wordbridge.project.entity.Extracurricular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtracurricularRepository extends JpaRepository<Extracurricular, Long> {

    List<Extracurricular> findByUserProfileId(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);

    void deleteByUserProfileId(Long userProfileId);


}
