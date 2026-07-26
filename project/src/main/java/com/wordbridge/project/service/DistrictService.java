package com.wordbridge.project.service;


import com.wordbridge.project.dto.requestdto.DistrictRequestDTO;
import com.wordbridge.project.dto.responsedto.DistrictResponseDTO;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DistrictService {
    DistrictResponseDTO save(DistrictRequestDTO d);

    List<DistrictResponseDTO> getAll();

    DistrictResponseDTO findById(Long id);

    void delete(Long id);

    DistrictResponseDTO update(Long id, DistrictRequestDTO d);

    List<DistrictResponseDTO> getDistrictByDivisionId(Long divisionId);

    List<DistrictResponseDTO> getDistrictByDivisionName(String divisionName);
}
