package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.EducationMapper;
import com.wordbridge.project.dto.requestdto.EducationRequestDTO;
import com.wordbridge.project.dto.responsedto.EducationResponseDTO;
import com.wordbridge.project.entity.Education;
import com.wordbridge.project.repository.EducationRepository;
import com.wordbridge.project.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {
    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;

    @Override
    public EducationResponseDTO save(EducationRequestDTO dto) {
        Education education = educationMapper.toEntity(dto);

        return educationMapper.toDTO(educationRepository.save(education));
    }

    @Override
    public List<EducationResponseDTO> getAll() {
        return educationRepository.findAll().stream().map(educationMapper::toDTO).toList();
    }

    @Override
    public EducationResponseDTO findById(Long id) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Education Found"));
        return educationMapper.toDTO(education);
    }

    @Override
    public EducationResponseDTO update(Long id, EducationRequestDTO dto) {

        Education existing = educationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User Education Not Found"));

        Education education = educationMapper.toEntity(dto);
        education.setId(existing.getId());

        education.setCreatedAt(existing.getCreatedAt());


        return educationMapper.toDTO(educationRepository.save(education));
    }

    @Override
    public void delete(Long id) {
        educationRepository.deleteById(id);
    }

    @Override
    public List<EducationResponseDTO> findByUserProfileId(Long userProfileId) {

        return educationRepository.findByUserProfileId(userProfileId).stream().map(educationMapper::toDTO).toList();
    }

    @Override
    public Long countByUserProfileId(Long userProfileId) {
        return educationRepository.countByUserProfileId(userProfileId);
    }
}
