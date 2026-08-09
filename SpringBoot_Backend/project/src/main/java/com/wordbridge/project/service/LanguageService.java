package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.LanguageRequestDTO;
import com.wordbridge.project.dto.responsedto.LanguageResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LanguageService {

    LanguageResponseDTO save(LanguageRequestDTO l);

    List<LanguageResponseDTO> getAll();

    LanguageResponseDTO findById(Long id);

    LanguageResponseDTO update(Long id, LanguageRequestDTO l);

    void delete(Long id);
}
