package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.UserSkillRequestDTO;
import com.wordbridge.project.dto.responsedto.UserSkillResponseDTO;
import com.wordbridge.project.entity.Skill;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.entity.UserSkill;
import com.wordbridge.project.repository.SkillRepository;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSkillMapper {

    private final SkillRepository skillRepository;

    private final UserProfileRepository userProfileRepository;

    public UserSkillResponseDTO toDTO(UserSkill us) {
        UserSkillResponseDTO dto = new UserSkillResponseDTO();

        dto.setId(us.getId());
        dto.setProficiencyLevel(us.getProficiencyLevel());
        dto.setYearsOfExperience(us.getYearsOfExperience());
        dto.setCreatedAt(us.getCreatedAt());

        dto.setUserProfileId(us.getUserProfile().getId());
        dto.setUserFullName(us.getUserProfile().getName());
        dto.setUserHeadline(us.getUserProfile().getHeadline());

        dto.setUserId(us.getUserProfile().getUser().getId());
        dto.setUserEmail(us.getUserProfile().getUser().getEmail());

        dto.setSkillId(us.getSkill().getId());
        dto.setSkillName(us.getSkill().getName());

        dto.setCategoryId(us.getSkill().getCategory().getId());
        dto.setCategoryName(us.getSkill().getCategory().getName());
        dto.setCategoryDescription(us.getSkill().getCategory().getDescription());


        return dto;
    }

    public UserSkill toEntity(UserSkillRequestDTO dto) {
        UserSkill us = new UserSkill();

        us.setProficiencyLevel(dto.getProficiencyLevel());
        us.setYearsOfExperience(dto.getYearsOfExperience());

        UserProfile userProfile = userProfileRepository.findById(dto.getUserProfileId()).orElseThrow(() -> new RuntimeException("No User Profile Found By this Id"));
        us.setUserProfile(userProfile);

        Skill skill = skillRepository.findById(dto.getSkillId()).orElseThrow(() -> new RuntimeException("No Skill Found By This Id"));
        us.setSkill(skill);


        return us;
    }

}
