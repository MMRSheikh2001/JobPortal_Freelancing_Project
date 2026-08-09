package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.ExtracurricularRequestDTO;
import com.wordbridge.project.dto.responsedto.ExtracurricularResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExtracurricularService {

    ExtracurricularResponseDTO save(ExtracurricularRequestDTO dto);

    List<ExtracurricularResponseDTO> getAll();

    ExtracurricularResponseDTO findById(Long id);

    ExtracurricularResponseDTO update(
            Long id,
            ExtracurricularRequestDTO dto
    );

    void delete(Long id);

    List<ExtracurricularResponseDTO> findByUserProfileId(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);


}
