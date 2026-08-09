package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.LanguageMapper;
import com.wordbridge.project.dto.requestdto.LanguageRequestDTO;
import com.wordbridge.project.dto.responsedto.LanguageResponseDTO;
import com.wordbridge.project.entity.Language;
import com.wordbridge.project.repository.LanguageRepository;
import com.wordbridge.project.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    private final LanguageMapper languageMapper;


    @Override
    public LanguageResponseDTO save(LanguageRequestDTO l) {
        Language language = languageMapper.toEntity(l);

        return languageMapper.toDTO(languageRepository.save(language));
    }

    @Override
    public List<LanguageResponseDTO> getAll() {
        return languageRepository.findAll().stream().map(languageMapper::toDTO).toList();
    }

    @Override
    public LanguageResponseDTO findById(Long id) {
        Language language = languageRepository.findById(id).orElseThrow(() -> new RuntimeException("No Language Found"));
        return languageMapper.toDTO(language);
    }

    @Override
    public LanguageResponseDTO update(Long id, LanguageRequestDTO l) {
        Language existing = languageRepository.findById(id).orElseThrow(() -> new RuntimeException("No language found"));


        Language language = languageMapper.toEntity(l);

        language.setId(existing.getId());


        return languageMapper.toDTO(languageRepository.save(language));
    }

    @Override
    public void delete(Long id) {
        languageRepository.deleteById(id);
    }
}
