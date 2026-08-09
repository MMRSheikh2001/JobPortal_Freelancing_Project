package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.CategoryRequestDTO;
import com.wordbridge.project.dto.responsedto.CategoryResponseDTO;
import com.wordbridge.project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories/")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> save(@RequestBody CategoryRequestDTO c) {
        CategoryResponseDTO savedCategory = categoryService.save(c);
        return ResponseEntity.ok(savedCategory);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public List<CategoryResponseDTO> getAll() {
        return categoryService.getAll();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("{id}")
    public ResponseEntity<CategoryResponseDTO> getById(@PathVariable Long id) {
        CategoryResponseDTO category = categoryService.findById(id);
        return ResponseEntity.ok(category);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok("Category Deleted");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<CategoryResponseDTO> update(@RequestBody CategoryRequestDTO c, @PathVariable Long id) {

        CategoryResponseDTO updatedCategory = categoryService.update(id, c);
        return ResponseEntity.ok(updatedCategory);
    }


}
