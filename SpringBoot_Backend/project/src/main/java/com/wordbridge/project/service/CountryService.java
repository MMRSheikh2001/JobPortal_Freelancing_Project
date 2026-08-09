package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.CountryRequestDTO;
import com.wordbridge.project.dto.responsedto.CountryResponseDTO;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CountryService {
    CountryResponseDTO save(CountryRequestDTO c);
    List<CountryResponseDTO> getAll();
    CountryResponseDTO findById(Long id);
    void delete(Long id);

    CountryResponseDTO update(Long id, CountryRequestDTO dto);
}
