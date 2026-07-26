package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.PoliceStationRequestDTO;
import com.wordbridge.project.dto.responsedto.PoliceStationResponseDTO;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface PoliceStationService {
    PoliceStationResponseDTO save(PoliceStationRequestDTO ps);
    List<PoliceStationResponseDTO> getAll();
    PoliceStationResponseDTO getById(Long id);
    void delete(Long id);
    PoliceStationResponseDTO update(Long id, PoliceStationRequestDTO dto);

    List<PoliceStationResponseDTO> getPSByDistrictId(Long districtId);
    List<PoliceStationResponseDTO> getPSByDistrictName(String districtName);
}
