package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.UserSkillMapper;
import com.wordbridge.project.dto.requestdto.UserSkillRequestDTO;
import com.wordbridge.project.dto.responsedto.UserSkillResponseDTO;
import com.wordbridge.project.entity.UserSkill;
import com.wordbridge.project.repository.UserSkillRepository;
import com.wordbridge.project.service.UserSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSkillServiceImpl implements UserSkillService {

    private final UserSkillRepository userSkillRepository;

    private final UserSkillMapper userSkillMapper;


    @Override
    public UserSkillResponseDTO save(UserSkillRequestDTO us) {
        if (userSkillRepository.existsByUserProfileIdAndSkillId(us.getUserProfileId(), us.getSkillId())) {
            throw new RuntimeException("User Skill already exists");
        }
        UserSkill userSkill = userSkillMapper.toEntity(us);
        UserSkill saved = userSkillRepository.save(userSkill);
        return userSkillMapper.toDTO(saved);
    }

    @Override
    public List<UserSkillResponseDTO> getAll() {
        return userSkillRepository.findAll().stream().map(userSkillMapper::toDTO).toList();
    }

    @Override
    public UserSkillResponseDTO findById(Long id) {
        UserSkill userSkill = userSkillRepository.findById(id).orElseThrow(() -> new RuntimeException("No User Skill Found"));
        return userSkillMapper.toDTO(userSkill);
    }

    @Override
    public UserSkillResponseDTO update(
            Long id,
            UserSkillRequestDTO dto) {

        UserSkill existing = userSkillRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User Skill Not Found"));

        UserSkill existingSkill =
                userSkillRepository.findByUserProfileIdAndSkillId(
                        dto.getUserProfileId(),
                        dto.getSkillId()
                );

        if (existingSkill != null &&
                !existingSkill.getId().equals(id)) {

            throw new RuntimeException(
                    "User Skill already exists"
            );
        }


        UserSkill userSkill = userSkillMapper.toEntity(dto);

        userSkill.setId(existing.getId());

        userSkill.setCreatedAt(existing.getCreatedAt());

        UserSkill updated =
                userSkillRepository.save(userSkill);

        return userSkillMapper.toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        userSkillRepository.deleteById(id);
    }

    @Override
    public List<UserSkillResponseDTO> findByUserProfileId(Long userProfileId) {
        return userSkillRepository.findByUserProfileId(userProfileId).stream().map(userSkillMapper::toDTO).toList();
    }

    @Override
    public List<UserSkillResponseDTO> findBySkillId(Long skillId) {
        return userSkillRepository.findBySkillId(skillId).stream().map(userSkillMapper::toDTO).toList();
    }

    @Override
    public List<UserSkillResponseDTO> findBySkillCategoryId(Long categoryId) {
        return userSkillRepository.findBySkillCategoryId(categoryId).stream().map(userSkillMapper::toDTO).toList();
    }

    @Override
    public UserSkillResponseDTO findByUserProfileIdAndSkillId(Long userProfileId, Long skillId) {
        UserSkill userSkill = userSkillRepository.findByUserProfileIdAndSkillId(userProfileId, skillId);
        if (userSkill == null) {
            throw new RuntimeException("User Skill Not Found by this id");
        }
        return userSkillMapper.toDTO(userSkill);
    }

    @Override
    public void deleteByUserProfileIdAndSkillId(Long userProfileId, Long skillId) {
        userSkillRepository.deleteByUserProfileIdAndSkillId(userProfileId, skillId);
    }

    @Override
    public Long countSkillsByUserProfileId(Long userProfileId) {
        return userSkillRepository.countByUserProfileId(userProfileId);
    }
}
