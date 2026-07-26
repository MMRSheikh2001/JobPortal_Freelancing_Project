package com.wordbridge.project.repository;

import com.wordbridge.project.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findByUserProfileId(Long userProfileId);

    List<UserSkill> findBySkillId(Long skillId);

    List<UserSkill> findBySkillCategoryId(Long categoryId);

    UserSkill
    findByUserProfileIdAndSkillId(
            Long userProfileId,
            Long skillId
    );

    boolean existsByUserProfileIdAndSkillId(Long userProfileId, Long skillId);

    Long countByUserProfileId(Long userProfileId);

    void deleteByUserProfileId(Long userProfileId);

    void deleteByUserProfileIdAndSkillId(
            Long userProfileId,
            Long skillId
    );
}
