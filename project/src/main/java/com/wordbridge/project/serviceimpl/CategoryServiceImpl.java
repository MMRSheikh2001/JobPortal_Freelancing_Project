package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.CategoryMapper;
import com.wordbridge.project.dto.requestdto.CategoryRequestDTO;
import com.wordbridge.project.dto.responsedto.CategoryResponseDTO;
import com.wordbridge.project.entity.Category;
import com.wordbridge.project.repository.CategoryRepository;
import com.wordbridge.project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDTO save(CategoryRequestDTO c) {
        Category category = categoryMapper.toEntity(c);

        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponseDTO> getAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toDTO).toList();
    }

    @Override
    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category Not found with this id"));
        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category Not Found"));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        Category updated = categoryRepository.save(category);

        return categoryMapper.toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
