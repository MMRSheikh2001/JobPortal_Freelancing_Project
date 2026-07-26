package com.wordbridge.project.service;


import com.wordbridge.project.dto.requestdto.UserSkillRequestDTO;
import com.wordbridge.project.dto.responsedto.UserSkillResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserSkillService {
    UserSkillResponseDTO save(UserSkillRequestDTO us);

    List<UserSkillResponseDTO> getAll();

    UserSkillResponseDTO findById(Long id);

    UserSkillResponseDTO update(
            Long id,
            UserSkillRequestDTO dto
    );

    void delete(Long id);

    List<UserSkillResponseDTO> findByUserProfileId(Long userProfileId);

    List<UserSkillResponseDTO> findBySkillId(Long skillId);

    List<UserSkillResponseDTO> findBySkillCategoryId(Long categoryId);

    UserSkillResponseDTO
    findByUserProfileIdAndSkillId(
            Long userProfileId,
            Long skillId
    );

    void deleteByUserProfileIdAndSkillId(
            Long userProfileId,
            Long skillId
    );

    Long countSkillsByUserProfileId(Long userProfileId);


}
