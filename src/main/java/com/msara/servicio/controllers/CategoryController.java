package com.msara.servicio.controllers;

import com.msara.servicio.domain.entities.CategoryEntity;
import com.msara.servicio.services.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryEntity categoryReq) {
        try {
            CategoryEntity category = categoryService.createCategory(categoryReq);
            return ResponseEntity.status(201).body(category);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCategory(@PathVariable Long id) {
        try {
            CategoryEntity categoryFound = categoryService.findCategory(id);
            return ResponseEntity.status(200).body(categoryFound);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> findAllCategory() {
        try {
            List<CategoryEntity> categories = categoryService.findAllCategories();
            return ResponseEntity.status(200).body(categories);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }
}
