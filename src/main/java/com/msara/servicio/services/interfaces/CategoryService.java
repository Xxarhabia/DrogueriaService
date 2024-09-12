package com.msara.servicio.services.interfaces;

import com.msara.servicio.domain.entities.CategoryEntity;

import java.util.List;

public interface CategoryService {

    CategoryEntity createCategory(CategoryEntity category);

    CategoryEntity findCategory(Long id);

    List<CategoryEntity> findAllCategories();
}
