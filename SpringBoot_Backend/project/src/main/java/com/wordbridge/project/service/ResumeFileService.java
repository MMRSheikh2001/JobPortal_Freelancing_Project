package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.ResumeRequestDTO;
import com.wordbridge.project.dto.responsedto.ResumeFileResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ResumeFileService {
    ResumeFileResponseDTO save(ResumeRequestDTO dto, MultipartFile cv);
    List<ResumeFileResponseDTO> findAll();

    ResumeFileResponseDTO getById(Long id);


    void delete(Long id);

    ResumeFileResponseDTO findByUserProfileId(Long userProfileId);

    void deleteByUserProfileId(Long userProfileId);

    Boolean existsByUserProfileId(Long userProfileId);


}
