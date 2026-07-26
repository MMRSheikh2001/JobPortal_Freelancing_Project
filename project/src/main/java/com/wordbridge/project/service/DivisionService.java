package com.wordbridge.project.service;


import com.wordbridge.project.dto.requestdto.DivisionRequestDTO;
import com.wordbridge.project.dto.responsedto.DivisionResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DivisionService {

    DivisionResponseDTO save(DivisionRequestDTO dto);

    List<DivisionResponseDTO> getAll();

    DivisionResponseDTO getById(Long id);

    DivisionResponseDTO update(Long id, DivisionRequestDTO dto);

    void delete(Long id);

    List<DivisionResponseDTO> getDivisionByCountryId(Long countryId);

    List<DivisionResponseDTO> getDivisionByCountryName(String countryName);
}
