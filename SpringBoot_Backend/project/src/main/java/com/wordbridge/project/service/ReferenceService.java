package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.ReferenceRequestDTO;
import com.wordbridge.project.dto.responsedto.ReferenceResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReferenceService {
    ReferenceResponseDTO save(ReferenceRequestDTO dto);

    List<ReferenceResponseDTO> getAll();

    ReferenceResponseDTO findById(Long id);

    ReferenceResponseDTO update(
            Long id,
            ReferenceRequestDTO dto
    );

    void delete(Long id);

    List<ReferenceResponseDTO> findByUserProfileId(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);

}
