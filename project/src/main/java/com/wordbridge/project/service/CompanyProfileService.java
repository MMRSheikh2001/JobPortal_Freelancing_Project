package com.wordbridge.project.service;


import com.wordbridge.project.dto.requestdto.CompanyProfileRequestDTO;
import com.wordbridge.project.dto.responsedto.CompanyProfileResponseDTO;


import com.wordbridge.project.job.CompanySearchRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CompanyProfileService {
    CompanyProfileResponseDTO save(CompanyProfileRequestDTO cp, MultipartFile image);

    List<CompanyProfileResponseDTO> getAll();

    CompanyProfileResponseDTO findById(Long id);

    void delete(Long id);

    void deleteImage(Long profileId);

    CompanyProfileResponseDTO update(Long id, CompanyProfileRequestDTO cp, MultipartFile image);

    CompanyProfileResponseDTO findByUserId(Long userId);

    List<CompanyProfileResponseDTO> findByLocationPoliceStationId(Long id);

    List<CompanyProfileResponseDTO> findByLocationPoliceStationDistrictId(Long id);

    Boolean existsByUserId(Long userId);

    Long count();

    List<CompanyProfileResponseDTO>  search(String keyword);

    Integer getProfileCompletionPercentage(Long userId);

    List<CompanyProfileResponseDTO> search(
            CompanySearchRequestDTO request
    );

}
