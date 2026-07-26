package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.CategoryRequestDTO;
import com.wordbridge.project.dto.responsedto.CategoryResponseDTO;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    CategoryResponseDTO save(CategoryRequestDTO c);

    List<CategoryResponseDTO> getAll();

    CategoryResponseDTO findById(Long id);

     CategoryResponseDTO update(Long id, CategoryRequestDTO dto);

    void delete(Long id);

}
