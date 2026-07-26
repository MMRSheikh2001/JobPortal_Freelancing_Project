package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.SkillRequestDTO;
import com.wordbridge.project.dto.responsedto.SkillResponseDTO;
import com.wordbridge.project.entity.Category;
import com.wordbridge.project.entity.Skill;
import com.wordbridge.project.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkillMapper {

    private final CategoryRepository categoryRepository;

    public SkillResponseDTO toDTO(Skill s) {
        SkillResponseDTO dto = new SkillResponseDTO();
        dto.setSkillId(s.getId());
        dto.setSkillName(s.getName());
        dto.setCategoryId(s.getCategory().getId());
        dto.setCategoryName(s.getCategory().getName());
        dto.setCategoryDescription(s.getCategory().getDescription());


        return dto;
    }

    public Skill toEntity(SkillRequestDTO dto) {
        Skill s = new Skill();
        s.setName(dto.getSkillName());
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("No Category Found By this Id"));
        s.setCategory(category);

        return s;
    }

}
