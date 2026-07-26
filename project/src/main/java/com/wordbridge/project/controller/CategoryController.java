package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.CategoryRequestDTO;
import com.wordbridge.project.dto.responsedto.CategoryResponseDTO;
import com.wordbridge.project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories/")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<CategoryResponseDTO> save(@RequestBody CategoryRequestDTO c) {
        CategoryResponseDTO savedCategory = categoryService.save(c);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping
    public List<CategoryResponseDTO> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryResponseDTO> getById(@PathVariable Long id) {
        CategoryResponseDTO category = categoryService.findById(id);
        return ResponseEntity.ok(category);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok("Category Deleted");
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryResponseDTO> update(@RequestBody CategoryRequestDTO c, @PathVariable Long id) {

        CategoryResponseDTO updatedCategory = categoryService.update(id, c);
        return ResponseEntity.ok(updatedCategory);
    }


}
