package com.wordbridge.project.service;


import com.wordbridge.project.dto.requestdto.TrainingRequestDTO;
import com.wordbridge.project.dto.responsedto.TrainingResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TrainingService {


    TrainingResponseDTO save(TrainingRequestDTO dto, MultipartFile file);

    List<TrainingResponseDTO> getAll();

    TrainingResponseDTO findById(Long id);

    TrainingResponseDTO update(
            Long id,
            TrainingRequestDTO dto
            , MultipartFile file
    );

    void delete(Long id);

    void deleteFile(Long id);

    List<TrainingResponseDTO> findByUserProfileId(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);


}
