package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.ExperienceMapper;
import com.wordbridge.project.dto.requestdto.ExperienceRequestDTO;
import com.wordbridge.project.dto.responsedto.ExperienceResponseDTO;
import com.wordbridge.project.entity.Experience;
import com.wordbridge.project.repository.ExperienceRepository;
import com.wordbridge.project.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final ExperienceMapper experienceMapper;

    @Override
    public ExperienceResponseDTO save(ExperienceRequestDTO dto) {
        Experience experience = experienceMapper.toEntity(dto);
        return experienceMapper.toDTO(experienceRepository.save(experience));
    }

    @Override
    public List<ExperienceResponseDTO> getAll() {
        return experienceRepository.findAll().stream().map(experienceMapper::toDTO).toList();
    }

    @Override
    public ExperienceResponseDTO findById(Long id) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Experience Found"));
        return experienceMapper.toDTO(experience);
    }

    @Override
    public ExperienceResponseDTO update(Long id, ExperienceRequestDTO dto) {
        Experience exist = experienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Experience Found"));
        Experience experience = experienceMapper.toEntity(dto);
        experience.setId(exist.getId());
        experience.setCreatedAt(exist.getCreatedAt());

        return experienceMapper.toDTO(experienceRepository.save(experience));
    }

    @Override
    public void delete(Long id) {
        experienceRepository.deleteById(id);
    }

    @Override
    public List<ExperienceResponseDTO> findByUserProfileId(Long userProfileId) {
        return experienceRepository.findByUserProfileId(userProfileId).stream().map(experienceMapper::toDTO).toList();
    }

    @Override
    public Long countByUserProfileId(Long userProfileId) {
        return experienceRepository.countByUserProfileId(userProfileId);
    }
}
