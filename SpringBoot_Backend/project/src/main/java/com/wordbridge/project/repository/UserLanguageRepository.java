package com.wordbridge.project.repository;

import com.wordbridge.project.entity.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {

    List<UserLanguage> findByUserProfileId(Long userProfileId);

    List<UserLanguage> findByLanguageId(Long languageId);


    UserLanguage
    findByUserProfileIdAndLanguageId(
            Long userProfileId,
            Long languageId
    );

    boolean existsByUserProfileIdAndLanguageId(Long userProfileId, Long languageId);

    Long countByUserProfileId(Long userProfileId);

    void deleteByUserProfileId(Long userProfileId);

    void deleteByUserProfileIdAndLanguageId(
            Long userProfileId,
            Long languageId
    );


}
