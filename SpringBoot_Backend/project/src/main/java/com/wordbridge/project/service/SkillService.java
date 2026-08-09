package com.wordbridge.project.service;


import com.wordbridge.project.dto.requestdto.SkillRequestDTO;
import com.wordbridge.project.dto.responsedto.SkillResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SkillService {
    SkillResponseDTO save(SkillRequestDTO s);

    List<SkillResponseDTO> getAll();

    SkillResponseDTO findById(Long id);

    void delete(Long id);

    SkillResponseDTO update(Long id,SkillRequestDTO s);

    List<SkillResponseDTO> getSkillByCategoryId(Long categoryId);

    List<SkillResponseDTO> getSkillByCategoryName(String categoryName);
}
