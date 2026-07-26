package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.PortfolioRequestDTO;
import com.wordbridge.project.dto.responsedto.PortfolioResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface PortfolioService {


    PortfolioResponseDTO save(PortfolioRequestDTO dto, MultipartFile file);

    List<PortfolioResponseDTO> getAll();

    PortfolioResponseDTO findById(Long id);

    PortfolioResponseDTO update(
            Long id,
            PortfolioRequestDTO dto
            , MultipartFile file
    );

    void delete(Long id);

    void deleteFile(Long id);

    List<PortfolioResponseDTO> findByUserProfileId(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);


}
