package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.EducationRequestDTO;
import com.wordbridge.project.dto.responsedto.EducationResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EducationService {


    EducationResponseDTO save(EducationRequestDTO dto);

    List<EducationResponseDTO> getAll();

    EducationResponseDTO findById(Long id);

    EducationResponseDTO update(
            Long id,
            EducationRequestDTO dto
    );

    void delete(Long id);

    List<EducationResponseDTO> findByUserProfileId(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);


}
