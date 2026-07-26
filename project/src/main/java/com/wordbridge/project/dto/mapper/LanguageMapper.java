package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.LanguageRequestDTO;
import com.wordbridge.project.dto.responsedto.LanguageResponseDTO;
import com.wordbridge.project.entity.Language;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {
    public LanguageResponseDTO toDTO(Language l) {
        LanguageResponseDTO dto = new LanguageResponseDTO();
        dto.setId(l.getId());
        dto.setName(l.getName());
        return dto;
    }

    public Language toEntity(LanguageRequestDTO dto) {
        Language l = new Language();
        l.setName(dto.getName());
        return l;
    }

}
