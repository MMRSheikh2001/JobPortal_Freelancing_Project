package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.ExperienceRequestDTO;
import com.wordbridge.project.dto.responsedto.ExperienceResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExperienceService {
    ExperienceResponseDTO save(ExperienceRequestDTO dto);

    List<ExperienceResponseDTO> getAll();

    ExperienceResponseDTO findById(Long id);

    ExperienceResponseDTO update(
            Long id,
            ExperienceRequestDTO dto
    );

    void delete(Long id);

    List<ExperienceResponseDTO> findByUserProfileId(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);


}
