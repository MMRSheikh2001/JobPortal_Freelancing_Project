package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.CategoryRequestDTO;
import com.wordbridge.project.dto.responsedto.CategoryResponseDTO;
import com.wordbridge.project.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponseDTO toDTO(Category c) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setDescription(c.getDescription());


        return dto;
    }

    public Category toEntity(CategoryRequestDTO dto) {
        Category c = new Category();
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());

        return c;
    }

}
