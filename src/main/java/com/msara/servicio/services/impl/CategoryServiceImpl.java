package com.msara.servicio.services.impl;

import com.msara.servicio.domain.entities.CategoryEntity;
import com.msara.servicio.domain.repositories.CategoryRepository;
import com.msara.servicio.services.interfaces.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    @Override
    public CategoryEntity findCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoryEntity> findAllCategories() {
        return categoryRepository.findAll();
    }
}
